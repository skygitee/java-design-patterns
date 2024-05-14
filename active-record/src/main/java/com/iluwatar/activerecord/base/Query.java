/*
 * This project is licensed under the MIT license. Module model-view-viewmodel is using ZK framework licensed under LGPL (see lgpl-3.0.txt).
 *
 * The MIT License
 * Copyright © 2014-2022 Ilkka Seppälä
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.iluwatar.activerecord.base;

import java.util.ArrayList;
import java.util.List;

final class Query {

  private Query() {
  }

  static SelectionQuery selectFrom(String tableName) {
    return new SelectionQuery(tableName);
  }

  static InsertionQuery insertInto(String tableName) {
    return new InsertionQuery(tableName);
  }

  static DeletionQuery deleteFrom(String tableName) {
    return new DeletionQuery(tableName);
  }

  static class SelectionQuery {
    private final String table;
    private final List<String> columns = new ArrayList<>();
    private final List<String> whereKeys = new ArrayList<>();

    SelectionQuery(String table) {
      this.table = table;
    }

    @Override
    public String toString() {
      return "SELECT "
          + (!columns.isEmpty() ? String.join(",", columns) : "*")
          + " FROM "
          + table
          + constructWhere(whereKeys);
    }

    SelectionQuery withKey(String column) {
      whereKeys.add(column);
      return this;
    }

  }

  static class InsertionQuery {

    private final String table;
    private final List<String> columns = new ArrayList<>();

    InsertionQuery(String table) {
      this.table = table;
    }

    @Override
    public String toString() {
      StringBuilder queryBuilder = new StringBuilder();
      queryBuilder.append("INSERT INTO ");
      queryBuilder.append(table);
      queryBuilder.append(" (");
      queryBuilder.append(String.join(",", columns));
      queryBuilder.append(") VALUES (");
      for (int i = 0; i < columns.size(); i++) {
        queryBuilder.append("?");
        if (i < columns.size() - 1) {
          queryBuilder.append(",");
        }
      }
      queryBuilder.append(")");
      return queryBuilder.toString();
    }

    InsertionQuery column(String column) {
      columns.add(column);
      return this;
    }
  }

  static class DeletionQuery {
    private final String table;
    private final List<String> whereKeys = new ArrayList<>();

    public DeletionQuery(String table) {
      this.table = table;
    }

    DeletionQuery withKey(String column) {
      whereKeys.add(column);
      return this;
    }

    @Override
    public String toString() {
      return "DELETE FROM "
          + table
          + constructWhere(whereKeys);
    }
  }

  private static String constructWhere(List<String> whereKeys) {
    if (whereKeys.isEmpty()) {
      return "";
    }

    return constructWhereFromKeys(whereKeys);
  }

  private static String constructWhereFromKeys(List<String> whereKeys) {
    StringBuilder whereClause = new StringBuilder(" WHERE ");
    for (String key : whereKeys) {
      whereClause.append(key)
          .append(" = ")
          .append("?");
    }
    return whereClause.toString();
  }
}
