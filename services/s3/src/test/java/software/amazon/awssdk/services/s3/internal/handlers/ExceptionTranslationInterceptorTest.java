/*
 * Copyright 2010-2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package software.amazon.awssdk.services.s3.internal.handlers;


import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.BeforeClass;
import org.junit.Test;
import software.amazon.awssdk.awscore.exception.AwsErrorDetails;
import software.amazon.awssdk.core.interceptor.Context;
import software.amazon.awssdk.core.interceptor.ExecutionAttributes;
import software.amazon.awssdk.core.interceptor.InterceptorContext;
import software.amazon.awssdk.core.internal.interceptor.DefaultFailedExecutionContext;
import software.amazon.awssdk.http.SdkHttpFullResponse;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

public class ExceptionTranslationInterceptorTest {

    private static ExceptionTranslationInterceptor interceptor;

    @BeforeClass
    public static void setup() {
        interceptor = new ExceptionTranslationInterceptor();
    }

    @Test
    public void headBucket404_shouldTranslateException() {
        S3Exception s3Exception = create404S3Exception();
        Context.FailedExecution failedExecution =
            new DefaultFailedExecutionContext(InterceptorContext.builder()
                                                                .request(HeadBucketRequest.builder().build())
                                                                .build(), s3Exception);

        assertThatThrownBy(() -> interceptor.onExecutionFailure(failedExecution, new ExecutionAttributes()))
            .isInstanceOf(NoSuchBucketException.class);
    }

    @Test
    public void headObject404_shouldTranslateException() {
        S3Exception s3Exception = create404S3Exception();
        Context.FailedExecution failedExecution =
            new DefaultFailedExecutionContext(InterceptorContext.builder()
                                                                .request(HeadObjectRequest.builder().build())
                                                                .build(), s3Exception);

        assertThatThrownBy(() -> interceptor.onExecutionFailure(failedExecution, new ExecutionAttributes()))
            .isInstanceOf(NoSuchKeyException.class);
    }

    @Test
    public void headObjectOtherException_shouldNotThrowException() {
        S3Exception s3Exception = (S3Exception) S3Exception.builder().statusCode(500).build();
        Context.FailedExecution failedExecution =
            new DefaultFailedExecutionContext(InterceptorContext.builder()
                                                                .request(HeadObjectRequest.builder().build())
                                                                .build(), s3Exception);

        interceptor.onExecutionFailure(failedExecution, new ExecutionAttributes());
    }

    @Test
    public void otherRequest_shouldNotThrowException() {
        S3Exception s3Exception = create404S3Exception();
        Context.FailedExecution failedExecution =
            new DefaultFailedExecutionContext(InterceptorContext.builder()
                                                                .request(PutObjectRequest.builder().build())
                                                                .build(), s3Exception);

        interceptor.onExecutionFailure(failedExecution, new ExecutionAttributes());
    }

    private S3Exception create404S3Exception() {
        return (S3Exception) S3Exception.builder()
                                        .awsErrorDetails(AwsErrorDetails.builder()
                                                                        .sdkHttpResponse(SdkHttpFullResponse.builder()
                                                                                                            .build())
                                                                        .build())
                                        .statusCode(404)
                                        .build();
    }
}
