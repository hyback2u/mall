package com.wxl.mall.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxl.common.utils.PageUtils;
import com.wxl.common.utils.Query;
import com.wxl.mall.member.dao.MemberDao;
import com.wxl.mall.member.dao.MemberLevelDao;
import com.wxl.mall.member.entity.MemberEntity;
import com.wxl.mall.member.entity.MemberLevelEntity;
import com.wxl.mall.member.exception.PhoneExistException;
import com.wxl.mall.member.exception.UsernameExistException;
import com.wxl.mall.member.service.MemberService;
import com.wxl.mall.member.vo.MemberLoginVO;
import com.wxl.mall.member.vo.MemberRegisterVO;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;


@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {
    @Resource
    private MemberLevelDao memberLevelDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    /**
     * 注册
     *
     * @param vo vo
     */
    @Override
    public void register(MemberRegisterVO vo) {
        MemberEntity entity = new MemberEntity();

        // 1、设置默认等级
        MemberLevelEntity memberLevelEntity = memberLevelDao.getDefaultLevel();
        entity.setLevelId(memberLevelEntity.getId());

        // 2、获取前端传递过来的信息
        entity.setMobile(vo.getPhone());
        entity.setUsername(vo.getUserName());

        // 密码要进行加密存储, 不能存储页面传过来的明文信息数据
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode(vo.getPassword());
        entity.setPassword(encode);

        // 其它的默认信息...

        // 持久化保存
        this.baseMapper.insert(entity);
    }


    /**
     * 检查手机号是否唯一
     * todo & fixme:Java基础知识异常机制的内容...
     *
     * @param phone 手机号
     */
    @Override
    public void checkPhoneUnique(String phone) throws PhoneExistException {
        MemberDao memberDao = this.baseMapper;
        Integer mobileCount = memberDao.selectCount(new QueryWrapper<MemberEntity>()
                .eq("mobile", phone));
        if (mobileCount > 0) {
            throw new PhoneExistException();
        }
    }


    /**
     * 检查用户名是否唯一
     *
     * @param username 用户名
     */
    @Override
    public void checkUsernameUnique(String username) throws UsernameExistException {
        MemberDao memberDao = this.baseMapper;
        Integer usernameCount = memberDao.selectCount(new QueryWrapper<MemberEntity>()
                .eq("username", username));
        if (usernameCount > 0) {
            throw new UsernameExistException();
        }
    }


    /**
     * 登录功能
     *
     * @param vo MemberLoginVO
     * @return MemberEntity
     */
    @Override
    public MemberEntity login(MemberLoginVO vo) {
        String loginAccount = vo.getLoginAccount();
        String password = vo.getPassword();

        // 1、去数据库查询
        MemberDao memberDao = this.baseMapper;

        MemberEntity memberEntity = memberDao.selectOne(new QueryWrapper<MemberEntity>()
                .eq("username", loginAccount).or()
                .eq("mobile", loginAccount));

        // 2、校验
        if (null == memberEntity) {
            return null;
        } else {
            // 数据库的密码字段
            String dbPassword = memberEntity.getPassword();
            // 密码匹配
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            boolean matches = passwordEncoder.matches(password, dbPassword);
            if (matches) {
                return memberEntity;
            } else {
                return null;
            }
        }
    }
}
