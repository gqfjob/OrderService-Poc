package com.example.orderservice;

import feign.Headers;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

//@FeignClient(name="product-service", configuration = ProductServiceClient.MultipartSupportConfig.class)
@FeignClient(name="product-service")
public interface ProductServiceClient {

    @RequestMapping(method= RequestMethod.GET, value="/product/all")
    public Map<Integer, String> getProductDetails();

    @RequestMapping(method= RequestMethod.POST, value="/product/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadFile(@RequestPart("uploadfile") MultipartFile uploadfile);

    @GetMapping(value = "/product/download/{fileName}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity download(@PathVariable("fileName") final String fileName) throws IOException;

    @Configuration
    public class MultipartSupportConfig {
       /* @Bean
        @Primary
        @Scope("prototype")
        public Encoder encoder() {
            return new FeignSpringFormEncoder();
        }*/

        @Autowired
        private ObjectFactory<HttpMessageConverters> messageConverters;

        @Bean
        public Encoder feignFormEncoder() {
            return new SpringFormEncoder(new SpringEncoder(messageConverters));
        }

        @Bean
        @Primary
        @Scope("prototype")
        public Decoder decoder() {
            Decoder decoder = (response, type) -> {
                if (type instanceof Class && MultipartFile.class.isAssignableFrom((Class) type)) {
                    Collection<String> contentTypes = response.headers().get("content-type");
                    String contentType = "application/octet-stream";
                    if (contentTypes.size() > 0) {
                        String[] temp = new String[contentTypes.size()];
                        contentTypes.toArray(temp);
                        contentType = temp[0];
                    }


                    byte[] bytes = StreamUtils.copyToByteArray(response.body().asInputStream());
                    InMemoryMultipartFile inMemoryMultipartFile = new InMemoryMultipartFile("file","", contentType,bytes);
                    return inMemoryMultipartFile;
                }
                return new SpringDecoder(messageConverters).decode(response, type);
            };
            return new ResponseEntityDecoder(decoder);
        }
    }
}
