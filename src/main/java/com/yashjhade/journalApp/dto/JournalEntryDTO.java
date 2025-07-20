package com.yashjhade.journalApp.dto;

import com.yashjhade.journalApp.enums.Sentiment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JournalEntryDTO {
    private String id; // Now a simple string!

    @NotEmpty
    @Schema(description = "The user's JE title")
    private String title;
    private String content;
    private Date date;

    @NotEmpty
    private Sentiment sentiment;
}