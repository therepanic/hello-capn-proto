package com.example;

import org.capnproto.*;
import org.capnproto.BufferedOutputStream;
import org.capnproto.BufferedOutputStreamWrapper;
import org.capnproto.BufferedInputStream;
import org.capnproto.BufferedInputStreamWrapper;
import org.capnproto.SerializePacked;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.nio.channels.ReadableByteChannel;

public class MessagingExample {
    public static void main(String[] args) throws IOException {
        // 1) Build the message
        MessageBuilder messageBuilder = new MessageBuilder();
        com.example.capnp.messaging.MessagingCapnp.Chat.Builder chatBuilder =
                messageBuilder.initRoot(com.example.capnp.messaging.MessagingCapnp.Chat.factory);
        chatBuilder.setId(777L);
        chatBuilder.setTitle("Hello World Chat");
        int numMessages = 1;
        org.capnproto.StructList.Builder<com.example.capnp.messaging.MessagingCapnp.Message.Builder> msgs =
                chatBuilder.initMessages(numMessages);

        com.example.capnp.messaging.MessagingCapnp.Message.Builder m = msgs.get(0);
        m.setId(42L);
        com.example.capnp.messaging.MessagingCapnp.User.Builder u = m.initSender();
        u.setId(1234);
        u.setUsername("alice");
        m.setContent("Hello!");

        // 2) Serialize to bytes via WritableByteChannel → BufferedOutputStreamWrapper
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        WritableByteChannel writeChannel = Channels.newChannel(baos);
        BufferedOutputStream capnBufOut = new BufferedOutputStreamWrapper(writeChannel);

        SerializePacked.write(capnBufOut, messageBuilder);
        capnBufOut.flush();

        byte[] packedData = baos.toByteArray();
        System.out.println("Serialized (packed) length = " + packedData.length + " bytes");

        // 3) Deserialize from bytes via ReadableByteChannel → BufferedInputStreamWrapper
        ByteArrayInputStream bais = new ByteArrayInputStream(packedData);
        ReadableByteChannel readChannel = Channels.newChannel(bais);
        BufferedInputStream capnBufIn = new BufferedInputStreamWrapper(readChannel);

        MessageReader messageReader = SerializePacked.read(capnBufIn);

        // 4) Read back and print
        com.example.capnp.messaging.MessagingCapnp.Chat.Reader chatR =
                messageReader.getRoot(com.example.capnp.messaging.MessagingCapnp.Chat.factory);

        System.out.println("Chat ID: " + chatR.getId());
        if (chatR.hasTitle()) {
            System.out.println("Chat Title: " + chatR.getTitle().toString());
        }

        if (chatR.hasMessages()) {
            org.capnproto.StructList.Reader<com.example.capnp.messaging.MessagingCapnp.Message.Reader> readMsgs =
                    chatR.getMessages();
            System.out.println("Messages count = " + readMsgs.size());
            for (int i = 0; i < readMsgs.size(); i++) {
                com.example.capnp.messaging.MessagingCapnp.Message.Reader mr = readMsgs.get(i);
                System.out.println("    Message #" + i + ": id=" + mr.getId());
                if (mr.hasSender()) {
                    com.example.capnp.messaging.MessagingCapnp.User.Reader ur = mr.getSender();
                    System.out.println("        Sender id=" + ur.getId() + ", username=" + ur.getUsername().toString());
                }
                if (mr.hasContent()) {
                    System.out.println("        Content=\"" + mr.getContent().toString() + "\"");
                }
            }
        }
    }
}
