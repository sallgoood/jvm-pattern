package it.is.all.good;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.time.LocalDate;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserExportWriterTest {
    @InjectMocks
    private OrderExportWriter contentWriter;

    @Mock
    private OrderRepository repo;

    @Test
    public void writesExpectedContent() throws IOException {
        Writer w = new StringWriter();
        when(repo.findByActiveTrue()).thenReturn(Stream.of(new Order(1L, emptyList(), LocalDate.of(2018, 1, 1))));
        contentWriter.writeOrders(w);
        assertEquals("OrderID;Date\n1;2018-01-01", w.toString());
    }
}
