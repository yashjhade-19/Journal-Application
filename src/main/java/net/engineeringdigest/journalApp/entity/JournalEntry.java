package net.engineeringdigest.journalApp.entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection="journal_entries")
@Data
@NoArgsConstructor   // Data annotation mei no args constructor nahi hota , aur jo required hai diserilizatoin i.e json to pojo, that why we use Noargsconstructor
public class JournalEntry {

    @Id
    private ObjectId id;
@NonNull
    private String title;

    private String content;

    private Date date;

}