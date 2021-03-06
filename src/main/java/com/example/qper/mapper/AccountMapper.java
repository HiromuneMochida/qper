package com.example.qper.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.example.qper.entity.AccountEntity;

/**
 * アカウントマッパー.
 */
@Mapper
public interface AccountMapper {

  /**
   * 登録済ユーザーを取得する.
   *
   * @param email メールアドレス
   * @return TBL.account
   */
  public AccountEntity identifyUser(String email);

}
