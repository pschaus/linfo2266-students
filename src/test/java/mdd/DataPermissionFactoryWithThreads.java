package mdd;

import com.github.guillaumederval.javagrading.Grade;

import java.io.FilePermission;
import java.security.PermissionCollection;
import java.security.Permissions;

public final class DataPermissionFactoryWithThreads implements Grade.PermissionCollectionFactory {
    @Override
    public PermissionCollection get() {
        PermissionCollection coll = new Permissions();
        coll.add(new RuntimePermission("modifyThreadGroup"));
        coll.add(new RuntimePermission("modifyThread"));
        coll.add(new FilePermission("<<ALL FILES>>", "read"));
        return coll;
    }
}
