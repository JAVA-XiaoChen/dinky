/*
 *
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package org.dinky.data.result;

import org.dinky.job.JobHandler;
import org.dinky.parser.SqlType;

import org.apache.flink.table.api.TableResult;

/**
 * ResultBuilder
 *
 * @since 2021/5/25 15:59
 */
public interface ResultBuilder {

    static ResultBuilder build(
            SqlType operationType,
            String id,
            Integer maxRowNum,
            boolean isChangeLog,
            boolean isAutoCancel,
            String timeZone) {
        switch (operationType) {
            case SELECT:
            case WITH:
                return new SelectResultBuilder(id, maxRowNum, isChangeLog, isAutoCancel, timeZone);
            case SHOW:
            case DESC:
            case DESCRIBE:
                return new ShowResultBuilder(id);
            case INSERT:
                return new InsertResultBuilder();
            case MOCKED_INSERT:
                return new MockResultBuilder(id, maxRowNum, isAutoCancel);
            default:
                return new DDLResultBuilder();
        }
    }

    IResult getResult(TableResult tableResult);

    /**
     * Get the results and store them persistently.
     *
     * @param tableResult table result
     * @param jobHandler  job handler
     * @return IResult
     */
    default IResult getResultWithPersistence(TableResult tableResult, JobHandler jobHandler) {
        return getResult(tableResult);
    }
}
