package com.nure.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Check;

import javax.persistence.*;
import java.time.LocalDateTime;

@Check(constraints = "sender_id <> receiver_id")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Slf4j
@Entity
@Table(name = "message")
public class Message implements Comparable<Message> {
    public Message(User userFrom, User userTo, String text, LocalDateTime sentTime){
        this.userFrom = userFrom;
        this.userTo = userTo;
        this.text = text;
        this.sentTime = sentTime;
    }

    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "sender_id", nullable = false)
    // @JsonManagedReference(value = "sent")
    private User userFrom;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "receiver_id", nullable = false)
    //@JsonManagedReference(value = "received")
    private User userTo;
    @Column(nullable = false)
    private String text;
    @Column(name = "sent_time", nullable = false)
    private LocalDateTime sentTime;

    @Override
    public int compareTo(Message message) {
        return this.sentTime.compareTo(message.sentTime);
    }
}
