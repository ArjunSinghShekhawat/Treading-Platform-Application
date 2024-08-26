package com.treadingPlatformApplication.exception;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
public class ResourceNotFoundException extends RuntimeException{
    private String resourceName;
    private String fieldName;
    private String fieldValue;

    public ResourceNotFoundException(String resourceName,String fieldName,String fieldValue){
        super(String.format("%s Not Found With %s : %s",resourceName,fieldName,fieldValue));
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.resourceName = resourceName;
    }
}
