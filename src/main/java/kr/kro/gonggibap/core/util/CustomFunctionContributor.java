package kr.kro.gonggibap.core.util;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.boot.model.FunctionContributor;

import static org.hibernate.type.StandardBasicTypes.DOUBLE;

/**
 * Hibernate에 사용자 정의 SQL 함수를 등록하는 클래스
 * MySQL full-text search에서 지원하는 MATCH ~ AGAINST는 JPQL FUNCTION에서 지원하지 않기 때문에 Custom 등록 필요
 */
public class CustomFunctionContributor implements FunctionContributor {

    private static final String FUNCTION_NAME_NATURAL = "match_against_natural";
    private static final String FUNCTION_PATTERN_NATURAL = "match (?1) against (?2 in NATURAL LANGUAGE MODE)";

    private static final String FUNCTION_NAME_BOOLEAN = "match_against_boolean";
    private static final String FUNCTION_PATTERN_BOOLEAN = "match (?1) against (?2 in BOOLEAN MODE)";

    @Override
    public void contributeFunctions(final FunctionContributions functionContributions) {
        // NATURAL LANGUAGE MODE 함수 등록
        functionContributions.getFunctionRegistry()
                .registerPattern(FUNCTION_NAME_NATURAL, FUNCTION_PATTERN_NATURAL,
                        functionContributions.getTypeConfiguration().getBasicTypeRegistry().resolve(DOUBLE));

        // BOOLEAN MODE 함수 등록
        functionContributions.getFunctionRegistry()
                .registerPattern(FUNCTION_NAME_BOOLEAN, FUNCTION_PATTERN_BOOLEAN,
                        functionContributions.getTypeConfiguration().getBasicTypeRegistry().resolve(DOUBLE));
    }
}
