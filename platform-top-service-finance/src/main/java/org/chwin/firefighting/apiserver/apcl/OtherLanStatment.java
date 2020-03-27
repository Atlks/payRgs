package org.chwin.firefighting.apiserver.apcl;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLCommentHint;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;

import java.util.List;
import java.util.Map;

public class OtherLanStatment  implements SQLStatement {
    String lan;
    private boolean setAfterSemiStt;

    public OtherLanStatment(String xxLanStr) {
        lan=xxLanStr;
    }

    @Override
    public String getDbType() {
        return null;
    }

    @Override
    public boolean isAfterSemi() {
        return false;
    }

    @Override
    public void setAfterSemi(boolean b) {
        setAfterSemiStt=b;
    }

    @Override
    public void accept(SQLASTVisitor sqlastVisitor) {

    }

    @Override
    public SQLStatement clone() {
        return null;
    }

    @Override
    public SQLObject getParent() {
        return null;
    }

    @Override
    public void setParent(SQLObject sqlObject) {

    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Object getAttribute(String s) {
        return null;
    }

    @Override
    public void putAttribute(String s, Object o) {

    }

    @Override
    public Map<String, Object> getAttributesDirect() {
        return null;
    }

    @Override
    public void output(StringBuffer stringBuffer) {

    }

    @Override
    public void addBeforeComment(String s) {

    }

    @Override
    public void addBeforeComment(List<String> list) {

    }

    @Override
    public List<String> getBeforeCommentsDirect() {
        return null;
    }

    @Override
    public void addAfterComment(String s) {

    }

    @Override
    public void addAfterComment(List<String> list) {

    }

    @Override
    public List<String> getAfterCommentsDirect() {
        return null;
    }

    @Override
    public boolean hasBeforeComment() {
        return false;
    }

    @Override
    public boolean hasAfterComment() {
        return false;
    }

    @Override
    public List<SQLObject> getChildren() {
        return null;
    }

    @Override
    public String toLowerCaseString() {
        return null;
    }

    @Override
    public List<SQLCommentHint> getHeadHintsDirect() {
        return null;
    }

    @Override
    public void setHeadHints(List<SQLCommentHint> list) {

    }

    public String toString() {
        return this.lan+(setAfterSemiStt?";":"");
    }
}
