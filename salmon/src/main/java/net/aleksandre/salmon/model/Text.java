package net.aleksandre.salmon.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@Table(name = "texts")
public class Text {

	@Id
	@GeneratedValue(generator = "sqlite")
	@TableGenerator(name = "sqlite", table = "sqlite_sequence", pkColumnName = "name", valueColumnName = "seq", pkColumnValue = "sqliteTestTable")
	private Integer id;
	private String text;
	private Long timestamp;

	public Text() {
	}

	public Text(String text) {
		this.text = text;
		timestamp = new Date().getTime();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "Id: " + id + ", text: " + text + ", timestamp: " + timestamp;
	}
}
