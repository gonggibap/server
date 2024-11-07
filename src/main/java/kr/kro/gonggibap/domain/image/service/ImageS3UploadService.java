package kr.kro.gonggibap.domain.image.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.DeleteObjectsResult;
import com.amazonaws.services.s3.model.ObjectMetadata;
import kr.kro.gonggibap.core.error.ErrorCode;
import kr.kro.gonggibap.core.exception.CustomException;
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
import java.util.List;

import static com.amazonaws.services.s3.model.DeleteObjectsRequest.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageS3UploadService {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public List<String> saveReviewFiles(List<MultipartFile> files) {
        return files.stream().map(file -> {
            try {
                String originalFilename = file.getOriginalFilename();
                String timestamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
                String newFilename = "review/" + timestamp + "_" + originalFilename;

                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(file.getSize());
                metadata.setContentType(file.getContentType());

                amazonS3.putObject(bucket, newFilename, file.getInputStream(), metadata);
                return amazonS3.getUrl(bucket, newFilename).toString();
            } catch (IOException e) {
                throw new CustomException(ErrorCode.FILE_UPLOAD_ERROR);
            }
        }).toList();
    }

    public void deleteReviewFiles(List<String> fileUrls) {
        String splitStr = ".com/";
        List<KeyVersion> keys = fileUrls.stream()
                .map(fileUrl -> {
                    String fileName = fileUrl.substring(fileUrl.lastIndexOf(splitStr) + splitStr.length());
                    return new KeyVersion(URLDecoder.decode(fileName, StandardCharsets.UTF_8));
                })
                .toList();

        DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucket).withKeys(keys);

        try {
            DeleteObjectsResult result = amazonS3.deleteObjects(deleteObjectsRequest);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.FILE_DELETE_ERROR);
        }

    }
}
