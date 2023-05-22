package service.impl;

import repository.AuditRepository;

public class AuditServiceImpl {

    private AuditRepository auditRepository;

    public AuditServiceImpl(AuditRepository auditRepository){
        this.auditRepository = auditRepository;
    }

    public void addCommandToFile(String command){
        auditRepository.writeAudit(command);
    }
}
