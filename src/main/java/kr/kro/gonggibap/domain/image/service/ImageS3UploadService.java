package kr.kro.gonggibap.domain.image.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageS3UploadService {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String saveReviewFile(MultipartFile multipartFile) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());

        String encodedFilename = URLEncoder.encode(originalFilename, StandardCharsets.UTF_8.name());
        String newFilename = "review/" + timestamp + "_" + originalFilename;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        amazonS3.putObject(bucket, newFilename, multipartFile.getInputStream(), metadata);
        return amazonS3.getUrl(bucket, newFilename).toString();
    }

    public void deleteReviewFile(String fileUrl) {
        String splitStr = ".com/";
        String fileName = fileUrl.substring(fileUrl.lastIndexOf(splitStr) + splitStr.length());

        String decodedFileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, decodedFileName));
    }
}
