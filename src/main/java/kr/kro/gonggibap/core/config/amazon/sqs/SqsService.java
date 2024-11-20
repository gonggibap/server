package kr.kro.gonggibap.core.config.amazon.sqs;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SqsService {
    private final AmazonSQS sqsClient;

    @Value("${sqs.queue.url}")
    private String queueUrl;

    // 메시지 전송
    public String sendMessage(String message) {
        try {
            SendMessageResult result = sqsClient.sendMessage(queueUrl, message);
            log.info("Message sent. ID: {}", result.getMessageId());
            return result.getMessageId();
        } catch (AmazonServiceException e) {
            log.error("AmazonServiceException occurred: {}", e.getErrorMessage());
            throw e;
        } catch (AmazonClientException e) {
            log.error("AmazonClientException occurred: {}", e.getMessage());
            throw e;
        }
    }

    // 메시지 수신
    public List<Message> receiveMessages(int maxMessages, int waitTimeSeconds) {
        try {
            ReceiveMessageRequest receiveRequest = new ReceiveMessageRequest()
                    .withQueueUrl(queueUrl)
                    .withMaxNumberOfMessages(maxMessages) // 한 번에 가져올 최대 메시지 개수
                    .withWaitTimeSeconds(waitTimeSeconds); // Long Polling 대기 시간

            List<Message> messages = sqsClient.receiveMessage(receiveRequest).getMessages();
            log.info("Received {} messages.", messages.size());
            return messages;
        } catch (AmazonServiceException e) {
            log.error("AmazonServiceException occurred: {}", e.getErrorMessage());
            throw e;
        } catch (AmazonClientException e) {
            log.error("AmazonClientException occurred: {}", e.getMessage());
            throw e;
        }
    }

    // 메시지 삭제
    public void deleteMessage(String receiptHandle) {
        try {
            sqsClient.deleteMessage(queueUrl, receiptHandle);
            log.info("Message deleted with receipt handle: {}", receiptHandle);
        } catch (AmazonServiceException e) {
            log.error("AmazonServiceException occurred: {}", e.getErrorMessage());
            throw e;
        } catch (AmazonClientException e) {
            log.error("AmazonClientException occurred: {}", e.getMessage());
            throw e;
        }
    }
}
