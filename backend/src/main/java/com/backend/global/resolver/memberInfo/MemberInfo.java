package com.backend.global.resolver.memberInfo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface MemberInfo {
// @MemberInfo 어노테이션이 붙은 MemberInfoDto 타입의 매개변수가 있을 때, 이 MemberInfoArgumentResolver가 동작

}
