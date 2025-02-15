package com.sparrow.chat.infrastructure.persistence;

import com.sparrow.chat.infrastructure.commons.PropertyAccessBuilder;
import com.sparrow.chat.infrastructure.commons.RedisKey;
import com.sparrow.chat.repository.QunRepository;
import com.sparrow.support.PlaceHolderParser;
import com.sparrow.support.PropertyAccessor;
import com.sparrow.utility.CollectionsUtility;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisQunRepository implements QunRepository {

    @Inject
    private RedisTemplate redisTemplate;

    @Override public List<Integer> getUserIdList(String qunId) {
        PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildByQunId(qunId);
        String userOfQunKey = PlaceHolderParser.parse(RedisKey.USER_ID_OF_QUN, propertyAccessor);
        List<String> originUserIds = this.redisTemplate.opsForList().range(userOfQunKey, 0, Integer.MAX_VALUE);
        if (CollectionsUtility.isNullOrEmpty(originUserIds)) {
            return Collections.emptyList();
        }
        List<Integer> userIds = new ArrayList<>(originUserIds.size());
        for (String userId : originUserIds) {
            userIds.add(Integer.parseInt(userId));
        }
        return userIds;
    }
}
