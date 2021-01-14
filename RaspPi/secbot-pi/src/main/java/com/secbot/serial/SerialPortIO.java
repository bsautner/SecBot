package com.secbot.serial;

import com.pi4j.io.serial.*;

import java.io.IOException;

public class SerialPortIO {

    private final SerialPortListener listener;
    private final String tty = "ttyACM0";
    final Serial serial = SerialFactory.createInstance();

    public SerialPortIO(SerialPortListener listener) {
        this.listener = listener;

    }

    public void write(String s) throws IOException {
        serial.write(s);
    }

    public void start() {




        serial.addListener(event -> {
            try {
                listener.onRead(event.getAsciiString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        try {
            SerialConfig config = new SerialConfig();


            config.device("/dev/" + tty)
                    .baud(Baud._9600)
                    .dataBits(DataBits._8)
                    .parity(Parity.NONE)
                    .stopBits(StopBits._1)
                    .flowControl(FlowControl.NONE);

            try {
                serial.open(config);
            } catch (IOException ex) {
                config = new SerialConfig();


                config.device("/dev/ttyACM1")
                        .baud(Baud._9600)
                        .dataBits(DataBits._8)
                        .parity(Parity.NONE)
                        .stopBits(StopBits._1)
                        .flowControl(FlowControl.NONE);
                serial.open(config);
            }


        }
        catch(IOException ex) {
          listener.onError(ex);
        }
    }

}
