import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Created by <a href="https://twitter.com/jaehoox">jaehoo</a> on 29/06/2018
 */
@RunWith(JUnit4.class)
@Slf4j
public class AvoidNulls {


    @Test
    public void avoidNulls() throws Exception {

        log.info("{}",getDiscountLine(new Customer(new MemberCard(60))));
        log.info("{}",getDiscountLine(new Customer(new MemberCard(10))));

    }

    public String getDiscountLine(Customer customer) {

        return "Discount%: " + getDiscountPercentage(customer.getMemberCard());

    }

    private Integer getDiscountPercentage(MemberCard card) {

        if (card.getFidelityPoints() >= 100) {

            return 5;

        }

        if (card.getFidelityPoints() >= 50) {

            return 3;

        }

        return null;

    }

}


@Data
@NoArgsConstructor
@AllArgsConstructor
class Customer{

    private MemberCard memberCard;

}

@Data
@NoArgsConstructor
@AllArgsConstructor
class MemberCard {
    private int fidelityPoints;
}