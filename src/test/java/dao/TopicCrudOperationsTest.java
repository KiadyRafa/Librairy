package dao;

import com.example.librairy.dao.TopicCrudOperations;
import com.example.librairy.entity.Topic;
import org.junit.jupiter.api.Test;
import com.example.librairy.dao.Criteria;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;

class TopicCrudOperationsTest {

    TopicCrudOperations subject = new TopicCrudOperations();

    @Test
    void read_all_topics_ok() {

        Topic expectedTopic = topicFantasy();


        List<Topic> actual = subject.findAll();


        assertTrue(actual.contains(expectedTopic));
    }

    @Test
    void read_topic_by_id_ok() {
        Topic expectedTopic = topicFantasy();

        Optional<Topic> actual = subject.findById(expectedTopic.getId());

        assertTrue(actual.isPresent());
        assertEquals(expectedTopic, actual.get());
    }

    @Test
    void create_then_update_topic_ok() {
        var topics = newTopic(100, "Random Topic");


        var actual = subject.save(topics);

        topics.setName("Updated Topic");
        var updatedTopic = subject.update(topics);

        var existingTopics = subject.findAll();
        assertEquals(topics.getName(), updatedTopic.getName());
        assertTrue(existingTopics.contains(updatedTopic));
    }

    @Test
    void read_topics_filter_by_name() {
        ArrayList<Criteria> criteria = new ArrayList<>();
        criteria.add(new Criteria("name", "fantasy"));
        List<Topic> expected = List.of(
                topicFantasy());

        // TODO: Implement findByCriteria in TopicCrudOperations
        List<Topic> actual = subject.findByCriteria(criteria);

        assertEquals(expected, actual);
        assertTrue(actual.stream()
                .allMatch(topic -> topic.getName().toLowerCase().contains("fantasy")));
    }

    @Test
    void read_topics_order_by_name() {
        assertThrows(UnsupportedOperationException.class, () -> {
            throw new UnsupportedOperationException("Not implemented yet");
        });
    }

    private Topic topicFantasy() {
        Topic expectedTopic = new Topic();
        expectedTopic.setId(1);
        expectedTopic.setName("Fantasy");
        return expectedTopic;
    }


    private Topic newTopic(int id, String name) {
        Topic topic = new Topic();
        topic.setId(id);
        topic.setName(name);
        return topic;
    }

}