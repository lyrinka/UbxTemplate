import com.example.project.twi.TwiDriver;
import com.example.project.twi.driver.TwiDelegateDriver;
import com.example.project.twi.driver.TwiDummyDriver;
import com.example.project.twi.exception.TwiDriverException;
import com.example.project.twi.transaction.TwiTransaction;
import com.example.project.twi.transaction.TwiTransactionSegment;

import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

public class Main {

    private static Logger createLogger() {
        Logger logger = Logger.getLogger("minimal");
        logger.setUseParentHandlers(false);
        ConsoleHandler handler = new java.util.logging.ConsoleHandler();
        handler.setFormatter(new java.util.logging.SimpleFormatter() {
            @Override
            public String format(java.util.logging.LogRecord record) {
                return record.getMessage() + "\n";
            }
        });
        logger.addHandler(handler);
        return logger;
    }

    public static void main(String[] args) throws TwiDriverException {

        Logger logger = createLogger();

        TwiDriver driver = new TwiDelegateDriver(new TwiDummyDriver("Dummy port", logger), "Delegator", logger);

        driver.open();

        try {

            TwiTransaction.builder()
                    .addSegment(
                            TwiTransactionSegment.write(
                                    0x03,
                                    0x40, 0x41
                            )
                    )
                    .addSegment(
                            TwiTransactionSegment.write(
                                    0x04,
                                    0x01, 0x02, 0x03
                            )
                    )
                    .addSegment(
                            TwiTransactionSegment.read(
                                    0x04,
                                    1
                            )
                    )
                    .build()
                    .submit(driver)
                    .getSegmentDataThen(2, data -> {
                        System.out.println("Data: " + data[0]);
                    });

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        driver.close();

    }

}