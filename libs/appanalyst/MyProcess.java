package silence.com.cn.a310application.appanalyst;

import java.io.OutputStream;

public class MyProcess {
    private final Process mProcess;

    public MyProcess(String name) throws MyException {
        try {
            mProcess = Runtime.getRuntime().exec(name);
        } catch (Exception e) {
            throw new MyException(e);
        }
    }

    public void exec(String cmd) throws MyException {
        OutputStream outputStream = mProcess.getOutputStream();
        try {
            outputStream.write((cmd + "\n").getBytes());
            outputStream.flush();
        } catch (Exception e) {
            throw new MyException(e);
        }
    }
}
