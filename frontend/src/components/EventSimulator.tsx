import React, { useState } from 'react';
import { Play, Zap, Check, AlertCircle, RefreshCw } from 'lucide-react';
import type { SensorType, SensorEventDTO } from '../types';

interface EventSimulatorProps {
  onEventIngested: () => void;
  apiBaseUrl: string;
}

export const EventSimulator: React.FC<EventSimulatorProps> = ({ onEventIngested, apiBaseUrl }) => {
  const [loading, setLoading] = useState(false);
  const [feedback, setFeedback] = useState<{ type: 'success' | 'error'; msg: string } | null>(null);

  const [type, setType] = useState<SensorType>('AIR_TEMPERATURE');
  const [deviceId, setDeviceId] = useState('sensor-temp-01');
  const [value, setValue] = useState('38.5');
  const [unit, setUnit] = useState('C');
  const [eventId, setEventId] = useState('');

  const handleSingleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setFeedback(null);

    const generatedEventId = eventId.trim() || `event-custom-${Date.now().toString().slice(-4)}`;
    
    let parsedVal: any = value;
    if (type !== 'EQUIPMENT_STATUS' && !isNaN(Number(value))) {
      parsedVal = Number(value);
    }

    const payload: SensorEventDTO = {
      eventId: generatedEventId,
      farmId: 'farm-001',
      deviceId,
      type,
      value: parsedVal,
      unit: type === 'EQUIPMENT_STATUS' ? null : unit,
      timestamp: new Date().toISOString()
    };

    try {
      const res = await fetch(`${apiBaseUrl}/api/v1/events`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });

      const data = await res.json();
      if (res.ok) {
        setFeedback({ type: 'success', msg: `Evento ${generatedEventId} ingerido com sucesso!` });
        setEventId('');
        onEventIngested();
      } else {
        setFeedback({ type: 'error', msg: `Erro [${res.status}]: ${data.message || data.error}` });
      }
    } catch (err: any) {
      setFeedback({ type: 'error', msg: `Falha na conexão com o servidor REST: ${err.message}` });
    } finally {
      setLoading(false);
    }
  };

  const handleBatchEditalRun = async () => {
    setLoading(true);
    setFeedback(null);

    const now = new Date();
    const editalEvents: SensorEventDTO[] = [
      { eventId: `event-001-${now.getTime()}`, farmId: 'farm-001', deviceId: 'sensor-temp-01', type: 'AIR_TEMPERATURE', value: 38.5, unit: 'C', timestamp: now.toISOString() },
      { eventId: `event-002-${now.getTime()}`, farmId: 'farm-001', deviceId: 'sensor-humidity-01', type: 'AIR_HUMIDITY', value: 24, unit: '%', timestamp: now.toISOString() },
      { eventId: `event-003-${now.getTime()}`, farmId: 'farm-001', deviceId: 'sensor-soil-01', type: 'SOIL_MOISTURE', value: 17, unit: '%', timestamp: now.toISOString() },
      { eventId: `event-004-${now.getTime()}`, farmId: 'farm-001', deviceId: 'reservoir-sensor-01', type: 'WATER_RESERVOIR_LEVEL', value: 12, unit: '%', timestamp: now.toISOString() },
      { eventId: `event-005-${now.getTime()}`, farmId: 'farm-001', deviceId: 'silo-sensor-01', type: 'SILO_LEVEL', value: 10, unit: '%', timestamp: now.toISOString() },
      { eventId: `event-006-${now.getTime()}`, farmId: 'farm-001', deviceId: 'irrigation-pump-01', type: 'EQUIPMENT_STATUS', value: 'FAILURE', unit: null, timestamp: now.toISOString() },
      { eventId: `event-007-${now.getTime()}`, farmId: 'farm-001', deviceId: 'sensor-temp-01', type: 'AIR_TEMPERATURE', value: 27, unit: 'C', timestamp: now.toISOString() }
    ];

    try {
      const res = await fetch(`${apiBaseUrl}/api/v1/events/batch`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(editalEvents)
      });

      if (res.ok) {
        setFeedback({ type: 'success', msg: 'Lote do Edital (7 eventos) executado! 6 alertas gerados e 1 evento normal mantido no histórico.' });
        onEventIngested();
      } else {
        const data = await res.json();
        setFeedback({ type: 'error', msg: `Erro no lote: ${data.message || data.error}` });
      }
    } catch (err: any) {
      setFeedback({ type: 'error', msg: `Falha ao enviar lote: ${err.message}` });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="glass-panel" style={{ padding: '24px', marginBottom: '28px' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px', flexWrap: 'wrap', gap: '12px' }}>
        <div>
          <h2 style={{ fontSize: '1.1rem', fontWeight: 700, display: 'flex', alignItems: 'center', gap: '8px' }}>
            <Zap size={20} color="#fbbf24" /> Simulador de Leituras de Sensores IoT
          </h2>
          <p style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>
            Simule disparos individuais ou execute o lote oficial de teste da Fazenda Boa Esperança.
          </p>
        </div>

        <button 
          onClick={handleBatchEditalRun} 
          disabled={loading}
          className="btn btn-primary"
          style={{ background: 'linear-gradient(135deg, #f59e0b 0%, #d97706 100%)', boxShadow: '0 4px 14px rgba(245, 158, 11, 0.4)' }}
        >
          {loading ? <RefreshCw className="animate-spin" size={16} /> : <Play size={16} />}
          Simular Lote do Edital (7 Eventos)
        </button>
      </div>

      {feedback && (
        <div style={{
          padding: '12px 16px',
          borderRadius: '10px',
          marginBottom: '20px',
          fontSize: '0.85rem',
          display: 'flex',
          alignItems: 'center',
          gap: '10px',
          background: feedback.type === 'success' ? 'rgba(16, 185, 129, 0.15)' : 'rgba(239, 68, 68, 0.15)',
          border: feedback.type === 'success' ? '1px solid rgba(16, 185, 129, 0.3)' : '1px solid rgba(239, 68, 68, 0.3)',
          color: feedback.type === 'success' ? '#34d399' : '#f87171'
        }}>
          {feedback.type === 'success' ? <Check size={18} /> : <AlertCircle size={18} />}
          <span>{feedback.msg}</span>
        </div>
      )}

      <form onSubmit={handleSingleSubmit} style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(180px, 1fr))', gap: '14px' }}>
        <div>
          <label style={{ fontSize: '0.75rem', fontWeight: 600, color: 'var(--text-muted)', marginBottom: '4px', display: 'block' }}>
            Tipo de Sensor
          </label>
          <select 
            value={type} 
            onChange={(e) => {
              const newType = e.target.value as SensorType;
              setType(newType);
              if (newType === 'AIR_TEMPERATURE') { setUnit('C'); setValue('38.5'); setDeviceId('sensor-temp-01'); }
              else if (newType === 'AIR_HUMIDITY') { setUnit('%'); setValue('24'); setDeviceId('sensor-humidity-01'); }
              else if (newType === 'SOIL_MOISTURE') { setUnit('%'); setValue('17'); setDeviceId('sensor-soil-01'); }
              else if (newType === 'WATER_RESERVOIR_LEVEL') { setUnit('%'); setValue('12'); setDeviceId('reservoir-sensor-01'); }
              else if (newType === 'SILO_LEVEL') { setUnit('%'); setValue('10'); setDeviceId('silo-sensor-01'); }
              else if (newType === 'EQUIPMENT_STATUS') { setUnit(''); setValue('FAILURE'); setDeviceId('irrigation-pump-01'); }
            }}
            className="form-control"
          >
            <option value="AIR_TEMPERATURE">AIR_TEMPERATURE (°C)</option>
            <option value="AIR_HUMIDITY">AIR_HUMIDITY (%)</option>
            <option value="SOIL_MOISTURE">SOIL_MOISTURE (%)</option>
            <option value="WATER_RESERVOIR_LEVEL">WATER_RESERVOIR_LEVEL (%)</option>
            <option value="SILO_LEVEL">SILO_LEVEL (%)</option>
            <option value="EQUIPMENT_STATUS">EQUIPMENT_STATUS</option>
          </select>
        </div>

        <div>
          <label style={{ fontSize: '0.75rem', fontWeight: 600, color: 'var(--text-muted)', marginBottom: '4px', display: 'block' }}>
            Valor Lido
          </label>
          <input 
            type="text" 
            value={value} 
            onChange={(e) => setValue(e.target.value)}
            className="form-control"
            placeholder="Ex: 38.5 ou FAILURE" 
          />
        </div>

        <div>
          <label style={{ fontSize: '0.75rem', fontWeight: 600, color: 'var(--text-muted)', marginBottom: '4px', display: 'block' }}>
            ID Dispositivo
          </label>
          <input 
            type="text" 
            value={deviceId} 
            onChange={(e) => setDeviceId(e.target.value)}
            className="form-control"
            placeholder="sensor-temp-01" 
          />
        </div>

        <div>
          <label style={{ fontSize: '0.75rem', fontWeight: 600, color: 'var(--text-muted)', marginBottom: '4px', display: 'block' }}>
            EventId (Opcional)
          </label>
          <input 
            type="text" 
            value={eventId} 
            onChange={(e) => setEventId(e.target.value)}
            className="form-control"
            placeholder="Auto-gerado se vazio" 
          />
        </div>

        <div style={{ display: 'flex', alignItems: 'flex-end' }}>
          <button type="submit" disabled={loading} className="btn btn-secondary" style={{ width: '100%' }}>
            Injetar Evento
          </button>
        </div>
      </form>
    </div>
  );
};
