package com.sparrow.chat.contact.assembler;

import com.sparrow.chat.contact.bo.QunBO;
import com.sparrow.chat.contact.bo.QunDetailWrapBO;
import com.sparrow.chat.contact.bo.QunPlazaBO;
import com.sparrow.chat.contact.protocol.enums.Category;
import com.sparrow.chat.contact.protocol.enums.ContactError;
import com.sparrow.chat.contact.protocol.enums.Nationality;
import com.sparrow.chat.contact.protocol.vo.CategoryVO;
import com.sparrow.chat.contact.protocol.vo.QunPlazaVO;
import com.sparrow.chat.contact.protocol.vo.QunVO;
import com.sparrow.exception.Asserts;
import com.sparrow.passport.protocol.dto.UserProfileDTO;
import com.sparrow.protocol.BusinessException;
import com.sparrow.utility.BeanUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named
public class QunAssembler {
    private static Logger logger = LoggerFactory.getLogger(QunAssembler.class);

    public QunVO assembleQun(QunDetailWrapBO qunDetail) throws BusinessException {
        QunBO qunBO = qunDetail.getQun();
        QunVO qunVo = new QunVO();
        this.assembleQun(qunBO, qunDetail.getOwner());
        return qunVo;
    }

    public QunVO assembleQun(QunBO qunBO, UserProfileDTO userProfile) throws BusinessException {
        QunVO qunVo = new QunVO();
        BeanUtility.copyProperties(qunBO, qunVo);
        qunVo.setQunId(qunBO.getId());
        Nationality nationality = Nationality.getById(qunBO.getNationalityId());
        Asserts.isTrue(nationality == null, ContactError.NATIONALITY_OF_QUN_EMPTY);
        qunVo.setNationality(nationality.getName());
        qunVo.setOwnerName(userProfile.getUserName());
        //todo
        Category category = Category.getById(qunBO.getCategoryId().intValue());
        Asserts.isTrue(category == null, ContactError.CATEGORY_OF_QUN_EMPTY);
        qunVo.setCategoryName(category.getName());
        return qunVo;
    }


    public QunPlazaVO assembleQunPlaza(QunPlazaBO qunPlaza) {
        QunPlazaVO qunPlazaVO = new QunPlazaVO();
        List<QunBO> qunList = qunPlaza.getQunList();
        Map<Long, UserProfileDTO> userDicts = qunPlaza.getUserDicts();

        Map<Integer, Category> categories = qunPlaza.getCategoryDicts();
        Map<Long, List<QunVO>> qunMap = new HashMap<>();
        for (QunBO qunBO : qunList) {
            QunVO qunVO = null;
            try {
                qunVO = this.assembleQun(qunBO, userDicts.get(qunBO.getOwnerId()));
            } catch (BusinessException e) {
                logger.error("qun assemble error qunId:{},qunName:{}", qunBO.getId(), qunBO.getName(), e);
                continue;
            }
            if (!qunMap.containsKey(qunVO.getCategoryId())) {
                qunMap.put(qunVO.getCategoryId(), new ArrayList<>());
            }
            qunMap.get(qunVO.getCategoryId()).add(qunVO);
        }
        qunPlazaVO.setQunMap(qunMap);

        Map<Integer, CategoryVO> categoryVOMap = new HashMap<>();
        for (Integer categoryId : categories.keySet()) {
            categoryVOMap.put(categoryId, this.assembleCategory(categories.get(categoryId)));
        }
        qunPlazaVO.setCategoryDicts(categoryVOMap);
        return qunPlazaVO;
    }

    private CategoryVO assembleCategory(Category category) {
        CategoryVO categoryVO = new CategoryVO();
        categoryVO.setId(category.getId());
        categoryVO.setCategoryName(category.getName());
        categoryVO.setDescription(category.getDescription());
        return categoryVO;
    }
}
