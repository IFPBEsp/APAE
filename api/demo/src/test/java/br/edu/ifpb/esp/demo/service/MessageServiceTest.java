package br.edu.ifpb.esp.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.edu.ifpb.esp.demo.domain.Message;
import br.edu.ifpb.esp.demo.repository.MessageRepository;

class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private MessageService messageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        List<Message> messages = Arrays.asList(
                new Message(1L, "Hello world 1"),
                new Message(2L, "Hello world 2")
        );
        when(messageRepository.findAll()).thenReturn(messages);

        List<Message> result = messageService.findAll();

        assertEquals(2, result.size());
        assertEquals("Hello world 1", result.get(0).getValue());
        verify(messageRepository, times(1)).findAll();
    }

    @Test
    void testFindById_Found() {
        Message message = new Message(1L, "Hi!");
        when(messageRepository.findById(1L)).thenReturn(Optional.of(message));

        Optional<Message> result = messageService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Hi!", result.get().getValue());
        verify(messageRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_NotFound() {
        when(messageRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Message> result = messageService.findById(99L);

        assertFalse(result.isPresent());
        verify(messageRepository, times(1)).findById(99L);
    }

    @Test
    void testSave() {
        Message message = new Message(null, "New hello world");
        Message savedMessage = new Message(1L, "New hello world");
        when(messageRepository.save(message)).thenReturn(savedMessage);

        Message result = messageService.save(message);

        assertNotNull(result.getId());
        assertEquals("New hello world", result.getValue());
        verify(messageRepository, times(1)).save(message);
    }

    @Test
    void testDeleteById() {
        Long id = 1L;
        doNothing().when(messageRepository).deleteById(id);

        messageService.deleteById(id);

        verify(messageRepository, times(1)).deleteById(id);
    }
}
