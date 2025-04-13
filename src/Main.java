import com.example.project.twi.TwiDriver;
import com.example.project.twi.driver.TwiDummyDriver;
import com.example.project.twi.exception.TwiDriverException;
import com.example.project.twi.transaction.TwiTransaction;
import com.example.project.twi.transaction.TwiTransactionSegment;

public class Main {

    public static void main(String[] args) throws TwiDriverException {

        TwiDriver driver = new TwiDummyDriver();

        driver.open();

        try {

            TwiTransaction.builder()
                    .addSegment(
                            TwiTransactionSegment.write(
                                    0x03,
                                    new byte[] {0x40, 0x41}
                            )
                    )
                    .addSegment(
                            TwiTransactionSegment.write(
                                    0x04,
                                    new byte[] {0x01, 0x02, 0x03}
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
                    .getData(2)
                    .ifPresent(data -> {
                        System.out.println("Data: " + data[0]);
                    });

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        driver.close();

    }

}