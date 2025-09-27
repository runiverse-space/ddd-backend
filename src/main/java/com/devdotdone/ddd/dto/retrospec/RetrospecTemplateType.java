package com.devdotdone.ddd.dto.retrospec;

/*
회고 템플릿 종류를 문자열이 아닌 타입으로 고정한다
템플릿 값이 늘어나거나 바뀌어도 Enum에서만 관리하여 유지보수가 쉽고, 안정성이 높아진다
 */
public enum RetrospecTemplateType {
    KTP,
    TIL,
    CSS
}
