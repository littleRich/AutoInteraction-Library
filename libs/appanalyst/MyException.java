package silence.com.cn.a310application.appanalyst;

public class MyException extends Exception {
    public MyException() {
        super();
    }

    public MyException(String detailMessage) {
        super(detailMessage);
    }

    public MyException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public MyException(Throwable throwable) {
        super(throwable);
    }
}
