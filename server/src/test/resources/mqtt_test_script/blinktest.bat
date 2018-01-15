@echo off
:: Script for drawer led testing
:: Test will not terminate

:: broker settings
:: 1883 is standard port
@SET BROKER_IP=localhost
@SET BROKER_PORT=1883

:: topic to publish to
@SET ON_TOPIC="devices/blink/start"
@SET OFF_TOPIC="devices/blink/stop"
:: message
@SET MESSAGE=

:: client settings
@SET CLIENT_ID="downshutter"
@SET MOSQ_BASE_DIR="C:\Program Files (x86)\mosquitto"

@SET DRAWER_COUNT=10
@SET LED_COUNT=2

:: This test will run forever
:loop
for /l %%x in (0, 1, %DRAWER_COUNT%) do (
   
   @echo Testing drawer %%x
   for /l %%c in (0, 1, %LED_COUNT%) do (
		if %%x GTR 0 (
		%MOSQ_BASE_DIR%"\mosquitto_pub.exe" -h %BROKER_IP% -p %BROKER_PORT% -i %CLIENT_ID% -t %OFF_TOPIC% -m "{ 'boxid' : %%x, 'color' : %%c }"
		)
		%MOSQ_BASE_DIR%"\mosquitto_pub.exe" -h %BROKER_IP% -p %BROKER_PORT% -i %CLIENT_ID% -t %ON_TOPIC% -m "{ 'boxid' : %%x, 'color' : %%c }"
	)
	
	:: sleep
	@SLEEP 1
)

goto loop

echo.