package com.coy.gupaoedu.study.mybatisplus.common.beanvalidation.validator;


import com.coy.gupaoedu.study.mybatisplus.common.beanvalidation.annotation.BVPattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.PatternSyntaxException;

/**
 * 扩展@Pattern，允许为空和为null
 *
 * @author chenck
 * @date 2019/9/10 11:57
 */
public class PatternValidator implements ConstraintValidator<BVPattern, CharSequence> {

    private java.util.regex.Pattern pattern;

    @Override
    public void initialize(BVPattern parameters) {
        BVPattern.Flag[] flags = parameters.flags();
        int intFlag = 0;
        for (BVPattern.Flag flag : flags) {
            intFlag = intFlag | flag.getValue();
        }

        try {
            pattern = java.util.regex.Pattern.compile(parameters.regexp(), intFlag);
        } catch (PatternSyntaxException e) {
            throw new IllegalArgumentException("@BVPattern.regexp() Invalid regular expression.");
        }
    }

    @Override
    public boolean isValid(CharSequence value,
                           ConstraintValidatorContext constraintValidatorContext) {
        // 允许为空和为null
        if (value == null || "".equals(value)) {
            return true;
        }
        Matcher m = pattern.matcher(value);
        return m.matches();
    }
}
