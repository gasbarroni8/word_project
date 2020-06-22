package com.ahuiali.word.json;

import com.ahuiali.word.pojo.Notebook;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ahui
 */
@Component
@Scope("prototype")
public class NotebookJson extends JsonBase {
    private Notebook notebook;

    private List<Notebook> notebooks;

    public List<Notebook> getNotebooks() {
        return notebooks;
    }

    public void setNotebooks(List<Notebook> notebooks) {
        this.notebooks = notebooks;
    }

    public void setNotebook(Notebook notebook) {
        this.notebook = notebook;
    }

    public Notebook getNotebook() {
        return notebook;
    }

    @Override
    public String toString() {
        return "NotebookJson{" +
                "notebook=" + notebook +
                ", notebooks=" + notebooks +
                ", code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
