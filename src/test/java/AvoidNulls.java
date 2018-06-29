import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;

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
        log.info("{}",getDiscountLine(new Customer()));

    }

    public String getDiscountLine(Customer customer) {

        return getDiscountPercentage(customer.getMemberCard())
                .map(d -> "Discount%: " + d).orElse("");

    }

    private Optional<Integer> getDiscountPercentage(MemberCard card) {

        if (card == null) {
            return empty();
        }

        if (card.getFidelityPoints() >= 100) {
            return of(5);
        }

        if (card.getFidelityPoints() >= 50) {
            return of(3);
        }

        return empty();
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