package com.github.samyuan1990.FabricJavaPool.api;

import com.github.samyuan1990.FabricJavaPool.ExecuteResult;

public interface FabricConnection {

    ExecuteResult query(String chainCode, String fcn, String... arguments) throws Exception;

    ExecuteResult invoke(String chainCode, String fcn, String... arguments) throws Exception;

}
