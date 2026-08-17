import React from 'react';
import { Activity, Radio, Phone, User, Home } from 'lucide-react';

interface NavbarProps {
  apiOnline: boolean;
}

export const Navbar: React.FC<NavbarProps> = ({ apiOnline }) => {
  return (
    <header className="glass-panel" style={{ borderRadius: '0 0 16px 16px', padding: '16px 32px', marginBottom: '28px' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '16px' }}>
        
        {/* Brand */}
        <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
          <div style={{
            background: 'var(--primary-gradient)',
            width: '42px',
            height: '42px',
            borderRadius: '12px',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            boxShadow: '0 4px 12px rgba(59, 130, 246, 0.4)'
          }}>
            <Activity color="#ffffff" size={24} />
          </div>
          <div>
            <h1 style={{ fontSize: '1.25rem', fontWeight: 800, letterSpacing: '-0.02em', background: 'linear-gradient(135deg, #fff 0%, #9ca3af 100%)', WebkitBackgroundClip: 'text', WebkitTextFillColor: 'transparent' }}>
              NotificationHub
            </h1>
            <span style={{ fontSize: '0.75rem', color: 'var(--text-muted)', fontWeight: 500 }}>
              Central de Notificações IoT • Cogito Lab
            </span>
          </div>
        </div>

        {/* Farm Metadata */}
        <div style={{ display: 'flex', alignItems: 'center', gap: '20px', background: 'rgba(15, 23, 42, 0.6)', padding: '8px 18px', borderRadius: '12px', border: '1px solid var(--border-color)' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '6px', fontSize: '0.85rem' }}>
            <Home size={16} color="#60a5fa" />
            <strong style={{ color: '#f3f4f6' }}>Fazenda Boa Esperança</strong>
            <span style={{ color: 'var(--text-dim)', fontSize: '0.75rem' }}>(farm-001)</span>
          </div>
          <div style={{ width: '1px', height: '16px', background: 'var(--border-color)' }} />
          <div style={{ display: 'flex', alignItems: 'center', gap: '6px', fontSize: '0.85rem', color: 'var(--text-muted)' }}>
            <User size={16} color="#34d399" />
            <span>João Silva</span>
          </div>
          <div style={{ width: '1px', height: '16px', background: 'var(--border-color)' }} />
          <div style={{ display: 'flex', alignItems: 'center', gap: '6px', fontSize: '0.85rem', color: 'var(--text-muted)' }}>
            <Phone size={16} color="#fbbf24" />
            <code>+55 35 99999-9999</code>
          </div>
        </div>

        {/* Status Indicator */}
        <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
          <div className="badge badge-info" style={{ gap: '8px', padding: '6px 14px' }}>
            <Radio size={14} color={apiOnline ? '#34d399' : '#f87171'} />
            <span style={{ color: apiOnline ? '#34d399' : '#f87171' }}>
              Backend REST API: {apiOnline ? 'ONLINE' : 'OFFLINE'}
            </span>
          </div>
        </div>

      </div>
    </header>
  );
};
