package it.is.all.good;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.jooq.lambda.Unchecked.consumer;

interface OrderRepository extends JpaRepository<Order, Long> {
    Stream<Order> findByActiveTrue();
}

interface UserRepository extends JpaRepository<User, Long> {
}

class User {

    private String username;
    private String firstName;
    private String lastName;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}

class Order {

    private long id;
    private LocalDate creationDate;

    public Order(long l, List<Object> emptyList, LocalDate of) {
        id = l;
        creationDate = of;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }
}

@Slf4j
class FileExporter {

    public File exportFile(String fileName, Consumer<Writer> contentWriter) throws Exception {
        File file = new File("export/" + fileName);
        try (Writer writer = new FileWriter(file)) {
            contentWriter.accept(writer);
            return file;
        } catch (Exception e) {
            // TODO send email notification
            log.debug("Gotcha!", e); // TERROR-Driven Development
            throw e;
        }
    }
}

class ClientCode {
    public static void main(String[] args) throws Exception {
        FileExporter fileExporter = new FileExporter();
        OrderExportWriter orderWriter = new OrderExportWriter();
        UserExportWriter userWriter = new UserExportWriter();

        fileExporter.exportFile("orders.csv", consumer(orderWriter::writeOrders));
        fileExporter.exportFile("users.csv", consumer(userWriter::writeUsers));
    }
}

class OrderExportWriter {

    private OrderRepository orderRepo;

    public void writeOrders(Writer writer) throws IOException {
        writer.write("OrderID;Date\n");
        orderRepo.findByActiveTrue()
                .map(o -> o.getId() + ";" + o.getCreationDate())
                .forEach(consumer(writer::write));
    }
}

class UserExportWriter {

    private UserRepository userRepo;

    public void writeUsers(Writer writer) throws IOException {
        writer.write("Username;FirstName;LastName\n");
        userRepo.findAll().stream()
                .map(u -> u.getUsername() + ";" + u.getFirstName() + ";" + u.getLastName())
                .forEach(consumer(writer::write));
    }
}
