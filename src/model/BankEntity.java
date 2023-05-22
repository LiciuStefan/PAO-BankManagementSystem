package model;

import java.util.Objects;

public abstract class BankEntity {
    private int entityId;
    private String entityName; //customer name, employee name, manager name

    public BankEntity(int entityId, String entityName) {
        this.entityId = entityId;
        this.entityName = entityName;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankEntity that = (BankEntity) o;
        return entityId == that.entityId && Objects.equals(entityName, that.entityName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entityId, entityName);
    }
}
