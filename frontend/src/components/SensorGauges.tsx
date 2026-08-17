import React from 'react';
import { Thermometer, Droplets, Sprout, Database, Archive, AlertTriangle, CheckCircle2 } from 'lucide-react';
import type { SensorEventDTO } from '../types';

interface SensorGaugesProps {
  events: SensorEventDTO[];
}

export const SensorGauges: React.FC<SensorGaugesProps> = ({ events }) => {

  const getLatestEvent = (type: string) => {
    return events.filter(e => e.type === type)[0] || null;
  };

  const tempEvt = getLatestEvent('AIR_TEMPERATURE');
  const humEvt = getLatestEvent('AIR_HUMIDITY');
  const soilEvt = getLatestEvent('SOIL_MOISTURE');
  const waterEvt = getLatestEvent('WATER_RESERVOIR_LEVEL');
  const siloEvt = getLatestEvent('SILO_LEVEL');
  const equipEvt = getLatestEvent('EQUIPMENT_STATUS');

  const sensors = [
    {
      title: 'Temperatura do Ar',
      type: 'AIR_TEMPERATURE',
      icon: Thermometer,
      latest: tempEvt,
      valueDisplay: tempEvt ? `${tempEvt.value} °C` : '-- °C',
      isAlert: tempEvt ? (Number(tempEvt.value) > 35) : false,
      threshold: '> 35 °C',
      unit: '°C'
    },
    {
      title: 'Umidade do Ar',
      type: 'AIR_HUMIDITY',
      icon: Droplets,
      latest: humEvt,
      valueDisplay: humEvt ? `${humEvt.value}%` : '-- %',
      isAlert: humEvt ? (Number(humEvt.value) < 30) : false,
      threshold: '< 30 %',
      unit: '%'
    },
    {
      title: 'Umidade do Solo',
      type: 'SOIL_MOISTURE',
      icon: Sprout,
      latest: soilEvt,
      valueDisplay: soilEvt ? `${soilEvt.value}%` : '-- %',
      isAlert: soilEvt ? (Number(soilEvt.value) < 20) : false,
      threshold: '< 20 %',
      unit: '%'
    },
    {
      title: 'Reservatório de Água',
      type: 'WATER_RESERVOIR_LEVEL',
      icon: Database,
      latest: waterEvt,
      valueDisplay: waterEvt ? `${waterEvt.value}%` : '-- %',
      isAlert: waterEvt ? (Number(waterEvt.value) < 15) : false,
      threshold: '< 15 %',
      unit: '%'
    },
    {
      title: 'Nível do Silo',
      type: 'SILO_LEVEL',
      icon: Archive,
      latest: siloEvt,
      valueDisplay: siloEvt ? `${siloEvt.value}%` : '-- %',
      isAlert: siloEvt ? (Number(siloEvt.value) < 15) : false,
      threshold: '< 15 %',
      unit: '%'
    },
    {
      title: 'Status Equipamento',
      type: 'EQUIPMENT_STATUS',
      icon: AlertTriangle,
      latest: equipEvt,
      valueDisplay: equipEvt ? String(equipEvt.value) : 'SEM INFORMAÇÃO',
      isAlert: equipEvt ? (String(equipEvt.value).toUpperCase() === 'FAILURE') : false,
      threshold: '= FAILURE',
      unit: ''
    }
  ];

  return (
    <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '16px', marginBottom: '28px' }}>
      {sensors.map((s) => {
        const IconComponent = s.icon;

        return (
          <div key={s.type} className="glass-panel" style={{
            padding: '20px',
            borderLeft: s.isAlert ? '4px solid #ef4444' : '4px solid #10b981',
            position: 'relative',
            overflow: 'hidden'
          }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '12px' }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                <div style={{
                  background: s.isAlert ? 'rgba(239, 68, 68, 0.15)' : 'rgba(16, 185, 129, 0.15)',
                  padding: '8px',
                  borderRadius: '10px'
                }}>
                  <IconComponent color={s.isAlert ? '#f87171' : '#34d399'} size={20} />
                </div>
                <div>
                  <h3 style={{ fontSize: '0.9rem', fontWeight: 600, color: 'var(--text-main)' }}>{s.title}</h3>
                  <span style={{ fontSize: '0.75rem', color: 'var(--text-dim)' }}>Alerta: {s.threshold}</span>
                </div>
              </div>

              {s.isAlert ? (
                <span className="badge badge-warning" style={{ fontSize: '0.65rem' }}>
                  <AlertTriangle size={12} /> ALERTA
                </span>
              ) : (
                <span className="badge badge-success" style={{ fontSize: '0.65rem' }}>
                  <CheckCircle2 size={12} /> NORMAL
                </span>
              )}
            </div>

            <div style={{ fontSize: '1.75rem', fontWeight: 800, color: s.isAlert ? '#f87171' : '#f3f4f6', margin: '8px 0' }}>
              {s.valueDisplay}
            </div>

            <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
              <span>Disp: <code>{s.latest ? s.latest.deviceId : 'N/A'}</code></span>
              <span>{s.latest ? new Date(s.latest.timestamp).toLocaleTimeString() : 'Sem leituras'}</span>
            </div>
          </div>
        );
      })}
    </div>
  );
};
