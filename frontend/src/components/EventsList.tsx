import React from 'react';
import { Database, Clock, Tag } from 'lucide-react';
import type { SensorEventDTO } from '../types';

interface EventsListProps {
  events: SensorEventDTO[];
}

export const EventsList: React.FC<EventsListProps> = ({ events }) => {
  return (
    <div className="glass-panel" style={{ padding: '24px', marginBottom: '28px' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '16px' }}>
        <h2 style={{ fontSize: '1.1rem', fontWeight: 700, display: 'flex', alignItems: 'center', gap: '8px' }}>
          <Database size={20} color="#60a5fa" /> Histórico de Eventos Ingeridos ({events.length})
        </h2>
      </div>

      <div style={{ overflowX: 'auto' }}>
        <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: '0.875rem' }}>
          <thead>
            <tr style={{ borderBottom: '1px solid var(--border-color)', textAlign: 'left', color: 'var(--text-muted)' }}>
              <th style={{ padding: '12px 16px' }}>Event ID</th>
              <th style={{ padding: '12px 16px' }}>Tipo de Sensor</th>
              <th style={{ padding: '12px 16px' }}>Valor / Unidade</th>
              <th style={{ padding: '12px 16px' }}>Dispositivo</th>
              <th style={{ padding: '12px 16px' }}>Data / Hora</th>
            </tr>
          </thead>
          <tbody>
            {events.length === 0 ? (
              <tr>
                <td colSpan={5} style={{ padding: '32px', textAlign: 'center', color: 'var(--text-dim)' }}>
                  Nenhum evento registrado no MongoDB.
                </td>
              </tr>
            ) : (
              events.map((evt) => (
                <tr key={evt.eventId} style={{ borderBottom: '1px solid rgba(255, 255, 255, 0.04)', transition: 'background 0.2s' }}>
                  <td style={{ padding: '12px 16px', fontWeight: 600 }}>
                    <code>{evt.eventId}</code>
                  </td>
                  <td style={{ padding: '12px 16px' }}>
                    <span className="badge badge-info" style={{ fontSize: '0.7rem' }}>
                      <Tag size={12} /> {evt.type}
                    </span>
                  </td>
                  <td style={{ padding: '12px 16px', fontWeight: 700, color: '#f3f4f6' }}>
                    {evt.value} {evt.unit ? evt.unit : ''}
                  </td>
                  <td style={{ padding: '12px 16px', color: 'var(--text-muted)' }}>
                    <code>{evt.deviceId}</code>
                  </td>
                  <td style={{ padding: '12px 16px', color: 'var(--text-dim)', fontSize: '0.8rem' }}>
                    <span style={{ display: 'flex', alignItems: 'center', gap: '6px' }}>
                      <Clock size={14} /> {new Date(evt.timestamp).toLocaleString()}
                    </span>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
};
