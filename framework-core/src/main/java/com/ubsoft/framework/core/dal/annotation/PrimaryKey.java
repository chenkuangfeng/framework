package com.ubsoft.framework.core.dal.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
@Retention(RetentionPolicy.RUNTIME)
public @interface PrimaryKey{
    String name();     
}
