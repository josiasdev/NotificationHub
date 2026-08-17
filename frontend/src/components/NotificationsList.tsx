import React from 'react';
import { Bell, AlertTriangle, Send, RefreshCw, Clock } from 'lucide-react';
import type { NotificationDTO } from '../types';

interface NotificationsListProps {
  notifications: NotificationDTO[];
}

export const NotificationsList: React.FC<NotificationsListProps> = ({ notifications }) => {
  return (
    <div className="glass-panel" style={{ padding: '24px' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '16px' }}>
        <h2 style={{ fontSize: '1.1rem', fontWeight: 700, display: 'flex', alignItems: 'center', gap: '8px' }}>
          <Bell size={20} color="#f59e0b" /> Histórico de Notificações / Alertas ({notifications.length})
        </h2>
      </div>

      <div style={{ overflowX: 'auto' }}>
        <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: '0.875rem' }}>
          <thead>
            <tr style={{ borderBottom: '1px solid var(--border-color)', textAlign: 'left', color: 'var(--text-muted)' }}>
              <th style={{ padding: '12px 16px' }}>Gravidade</th>
              <th style={{ padding: '12px 16px' }}>Mensagem Gerada</th>
              <th style={{ padding: '12px 16px' }}>Destinatário</th>
              <th style={{ padding: '12px 16px' }}>Status Envio</th>
              <th style={{ padding: '12px 16px' }}>Gerado em</th>
            </tr>
          </thead>
          <tbody>
            {notifications.length === 0 ? (
              <tr>
                <td colSpan={5} style={{ padding: '32px', textAlign: 'center', color: 'var(--text-dim)' }}>
                  Nenhuma notificação gerada até o momento.
                </td>
              </tr>
            ) : (
              notifications.map((notif) => {
                const isCritical = notif.severity === 'CRITICAL';
                return (
                  <tr key={notif.id} style={{ borderBottom: '1px solid rgba(255, 255, 255, 0.04)' }}>
                    <td style={{ padding: '12px 16px' }}>
                      <span className={isCritical ? 'badge badge-critical' : 'badge badge-warning'}>
                        <AlertTriangle size={12} /> {notif.severity}
                      </span>
                    </td>
                    <td style={{ padding: '12px 16px', fontWeight: 500, color: '#f3f4f6' }}>
                      {notif.message}
                    </td>
                    <td style={{ padding: '12px 16px', color: 'var(--text-muted)' }}>
                      <code>{notif.recipientPhone}</code>
                    </td>
                    <td style={{ padding: '12px 16px' }}>
                      {notif.status === 'SENT' && (
                        <span className="badge badge-success">
                          <Send size={12} /> ENVIADO (SENT)
                        </span>
                      )}
                      {notif.status === 'PENDING' && (
                        <span className="badge badge-pending">
                          <RefreshCw size={12} /> PENDENTE (RABBITMQ)
                        </span>
                      )}
                      {notif.status === 'FAILED' && (
                        <span className="badge badge-critical">
                          FALHOU ({notif.retryCount} retentativas)
                        </span>
                      )}
                      {notif.status === 'DLQ_ROUTED' && (
                        <span className="badge badge-critical" style={{ background: 'purple' }}>
                          ROTEADO DLQ
                        </span>
                      )}
                    </td>
                    <td style={{ padding: '12px 16px', color: 'var(--text-dim)', fontSize: '0.8rem' }}>
                      <span style={{ display: 'flex', alignItems: 'center', gap: '6px' }}>
                        <Clock size={14} /> {new Date(notif.createdAt).toLocaleString()}
                      </span>
                    </td>
                  </tr>
                );
              })
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
};
