package pl.com.bottega.ecommerce.sales.domain.invoicing;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class BookKeeperTest {

    Date date;
    ClientData klient;
    BookKeeper bookKeeper;

    ProductData apple;
    ProductData peniciline;
    ProductData pencil;

    List<RequestItem> noItems;
    List<RequestItem> someItems;

    @Before
    public void setup() {
        klient = new ClientData(Id.generate(), "Klient");
        bookKeeper = new BookKeeper();

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2015);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DAY_OF_MONTH, 5);
        date = cal.getTime();

        apple = new ProductData(Id.generate(), new Money(0.5), "apple", ProductType.FOOD, date);
        peniciline = new ProductData(Id.generate(), new Money(3), "peniciline", ProductType.DRUG, date);
        pencil = new ProductData(Id.generate(), new Money(1), "pencil", ProductType.STANDARD, date);

        noItems = new ArrayList<>();
        someItems = new ArrayList<>();
        someItems.add(new RequestItem(apple, 10, new Money(5)));
        someItems.add(new RequestItem(peniciline, 10, new Money(30)));
        someItems.add(new RequestItem(pencil, 10, new Money(10)));
    }

    @Test
    public void WithoutProductsThereIsNoProductsOnReturnedInvoice() {
        int nrOfProductsOnInvoice = bookKeeper.issuance(klient, noItems)
                                              .getItems()
                                              .size();
        assertThat(nrOfProductsOnInvoice, Matchers.comparesEqualTo(0));
    }

    @Test
    public void WithoutProductsThereIsNothigToPayOnReturnedInvoice() {
        Money money = bookKeeper.issuance(klient, noItems)
                                .getGros();
        assertThat(money.equals(new Money(0)), Matchers.comparesEqualTo(true));
    }

    @Test
    public void CheckPriceOnTheInvoice() {
        Money money = bookKeeper.issuance(klient, someItems)
                                .getGros();
        assertThat(money.equals(new Money(5 * 1.07 + 30 * 1.05 + 10 * 1.23)), Matchers.comparesEqualTo(true));
    }

    @Test
    public void CheckItemsOnTheInvoice() {
        List<InvoiceLine> items = bookKeeper.issuance(klient, someItems)
                                            .getItems();

        // .getProduct();
        assertThat(items,
                contains(hasProperty("product", is(apple)), hasProperty("product", is(peniciline)), hasProperty("product", is(pencil))));
    }
}
