package pl.com.bottega.ecommerce.sales.domain.invoicing;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;

public class BookKeeperTest {

    ClientData klient;
    List<RequestItem> items;
    BookKeeper bookKeeper;
    Invoice invoice;

    @Before
    public void setup() {
        klient = new ClientData(Id.generate(), "Klient");
        items = new ArrayList<>();
        bookKeeper = new BookKeeper();
        invoice = new Invoice(Id.generate(), klient);
    }

    @Test
    public void WithoutProductsThereIsNoProductsOnReturnedInvoice() {
        int nrOfProductsOnInvoice = bookKeeper.issuance(klient, items)
                                              .getItems()
                                              .size();
        assertEquals(nrOfProductsOnInvoice, 0);
    }

}
