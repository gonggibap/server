package kr.kro.gonggibap.core.util;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.boot.model.FunctionContributor;

import static org.hibernate.type.StandardBasicTypes.DOUBLE;
/**
 * Hibernate에 사용자 정의 SQL 함수를 등록하는 클래스
 * MySQL full-text search에서 지원하는 MATCH ~ AGAINST는 JPQL FUNCTION에서 지원하지 않기 때문에 Custom 등록 필요
 */
public class CustomFunctionContributor implements FunctionContributor {

    private static final String FUNCTION_NAME = "match_against";
    private static final String FUNCTION_PATTERN = "match (?1) against (?2 in NATURAL LANGUAGE MODE)";

    @Override
    public void contributeFunctions(final FunctionContributions functionContributions) {
        functionContributions.getFunctionRegistry()
                .registerPattern(FUNCTION_NAME, FUNCTION_PATTERN,
                        functionContributions.getTypeConfiguration().getBasicTypeRegistry().resolve(DOUBLE));
    }
}