package ui;

import modelservice.ModelService;
import modelservice.ModelService.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * UI principal: JFrame con pestañas para Clientes, Peliculas, Entradas y Facturas.
 */
public class UI extends JFrame {

    private JTabbedPane tabs;

    // Clientes
    private JTable tableClientes;
    private DefaultTableModel modelClientes;

    // Peliculas
    private JTable tablePeliculas;
    private DefaultTableModel modelPeliculas;

    // Entradas
    private JTable tableEntradas;
    private DefaultTableModel modelEntradas;

    // Facturas
    private JTable tableFacturas;
    private DefaultTableModel modelFacturas;

    public UI() {
        setTitle("Sistema Cine - CRUD");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tabs = new JTabbedPane();

        tabs.addTab("Clientes", clientesPanel());
        tabs.addTab("Películas", peliculasPanel());
        tabs.addTab("Entradas", entradasPanel());
        tabs.addTab("Facturas", facturasPanel());

        add(tabs);
        refreshAll();
    }

    private void refreshAll() {
        refreshClientesTable();
        refreshPeliculasTable();
        refreshEntradasTable();
        refreshFacturasTable();
    }

    // ----------------- CLIENTES -----------------
    private JPanel clientesPanel() {
        JPanel p = new JPanel(new BorderLayout());
        modelClientes = new DefaultTableModel(new String[]{"ID","Nombre","Documento","Teléfono"},0) {
            public boolean isCellEditable(int r, int c){ return false; }
        };
        tableClientes = new JTable(modelClientes);
        p.add(new JScrollPane(tableClientes), BorderLayout.CENTER);

        JPanel botones = new JPanel();
        JButton add = new JButton("Agregar");
        JButton edit = new JButton("Editar");
        JButton del = new JButton("Eliminar");
        JButton ref = new JButton("Refrescar");
        botones.add(add); botones.add(edit); botones.add(del); botones.add(ref);
        p.add(botones, BorderLayout.SOUTH);

        add.addActionListener(_ -> dialogCliente(null));
        edit.addActionListener(_ -> {
            int sel = tableClientes.getSelectedRow();
            if(sel==-1){ JOptionPane.showMessageDialog(this,"Selecciona un cliente"); return; }
            int id = (Integer) tableClientes.getValueAt(sel,0);
            dialogCliente(ModelService.getClienteById(id));
        });
        del.addActionListener(_ -> {
            int sel = tableClientes.getSelectedRow();
            if(sel==-1){ JOptionPane.showMessageDialog(this,"Selecciona un cliente"); return; }
            int id = (Integer) tableClientes.getValueAt(sel,0);
            if(JOptionPane.showConfirmDialog(this,"Eliminar cliente ID "+id+"?","Confirmar",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
                if(ModelService.deleteCliente(id)) refreshClientesTable();
                else JOptionPane.showMessageDialog(this,"Error al eliminar");
            }
        });
        ref.addActionListener(_ -> refreshClientesTable());

        return p;
    }

    private void dialogCliente(Cliente c) {
        JTextField fn = new JTextField(); JTextField fd = new JTextField(); JTextField ft = new JTextField();
        if(c!=null) { fn.setText(c.nombre); fd.setText(c.documento); ft.setText(c.telefono); }
        Object[] fields = {"Nombre:", fn, "Documento:", fd, "Teléfono:", ft};
        int r = JOptionPane.showConfirmDialog(this, fields, c==null?"Agregar Cliente":"Editar Cliente", JOptionPane.OK_CANCEL_OPTION);
        if(r==JOptionPane.OK_OPTION) {
            if(fn.getText().trim().isEmpty() || fd.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this,"Nombre y Documento son obligatorios"); return; }
            if(c==null) {
                Cliente n = new Cliente(); n.nombre=fn.getText().trim(); n.documento=fd.getText().trim(); n.telefono=ft.getText().trim();
                if(ModelService.insertCliente(n)) refreshClientesTable(); else JOptionPane.showMessageDialog(this,"Error al insertar (¿documento duplicado?)");
            } else {
                c.nombre=fn.getText().trim(); c.documento=fd.getText().trim(); c.telefono=ft.getText().trim();
                if(ModelService.updateCliente(c)) refreshClientesTable(); else JOptionPane.showMessageDialog(this,"Error al actualizar");
            }
        }
    }

    private void refreshClientesTable() {
        modelClientes.setRowCount(0);
        for(Cliente c : ModelService.getAllClientes()) modelClientes.addRow(new Object[]{c.id,c.nombre,c.documento,c.telefono});
    }

    // ----------------- PELICULAS -----------------
    private JPanel peliculasPanel() {
        JPanel p = new JPanel(new BorderLayout());
        modelPeliculas = new DefaultTableModel(new String[]{"ID","Título","Género","Valor Boleta"},0) {
            public boolean isCellEditable(int r, int c){ return false; }
        };
        tablePeliculas = new JTable(modelPeliculas);
        p.add(new JScrollPane(tablePeliculas), BorderLayout.CENTER);

        JPanel botones = new JPanel();
        JButton add = new JButton("Agregar");
        JButton edit = new JButton("Editar");
        JButton del = new JButton("Eliminar");
        JButton ref = new JButton("Refrescar");
        botones.add(add); botones.add(edit); botones.add(del); botones.add(ref);
        p.add(botones, BorderLayout.SOUTH);

        add.addActionListener(_ -> dialogPelicula(null));
        edit.addActionListener(_ -> {
            int sel = tablePeliculas.getSelectedRow(); if(sel==-1){ JOptionPane.showMessageDialog(this,"Selecciona una película"); return; }
            int id = (Integer) tablePeliculas.getValueAt(sel,0);
            dialogPelicula(ModelService.getPeliculaById(id));
        });
        del.addActionListener(_ -> {
            int sel = tablePeliculas.getSelectedRow(); if(sel==-1){ JOptionPane.showMessageDialog(this,"Selecciona una película"); return; }
            int id = (Integer) tablePeliculas.getValueAt(sel,0);
            if(JOptionPane.showConfirmDialog(this,"Eliminar película ID "+id+"?","Confirmar",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
                if(ModelService.deletePelicula(id)) refreshPeliculasTable(); else JOptionPane.showMessageDialog(this,"Error al eliminar");
            }
        });
        ref.addActionListener(_ -> refreshPeliculasTable());

        return p;
    }

    private void dialogPelicula(Pelicula p) {
        JTextField ft = new JTextField(); JTextField fg = new JTextField(); JTextField fv = new JTextField();
        if(p!=null){ ft.setText(p.titulo); fg.setText(p.genero); fv.setText(String.valueOf(p.valor_boleta)); }
        Object[] fields = {"Título:", ft, "Género:", fg, "Valor boleta:", fv};
        int r = JOptionPane.showConfirmDialog(this, fields, p==null?"Agregar Película":"Editar Película", JOptionPane.OK_CANCEL_OPTION);
        if(r==JOptionPane.OK_OPTION) {
            if(ft.getText().trim().isEmpty()){ JOptionPane.showMessageDialog(this,"Título obligatorio"); return;}
            double valor = 0; try{ valor = Double.parseDouble(fv.getText().trim()); }catch(Exception ex){ valor = 0; }
            if(p==null) {
                Pelicula np = new Pelicula(); np.titulo = ft.getText().trim(); np.genero = fg.getText().trim(); np.valor_boleta = valor;
                if(ModelService.insertPelicula(np)) refreshPeliculasTable(); else JOptionPane.showMessageDialog(this,"Error al insertar");
            } else {
                p.titulo = ft.getText().trim(); p.genero = fg.getText().trim(); p.valor_boleta = valor;
                if(ModelService.updatePelicula(p)) refreshPeliculasTable(); else JOptionPane.showMessageDialog(this,"Error al actualizar");
            }
        }
    }

    private void refreshPeliculasTable() {
        modelPeliculas.setRowCount(0);
        for(Pelicula p : ModelService.getAllPeliculas()) modelPeliculas.addRow(new Object[]{p.id,p.titulo,p.genero,p.valor_boleta});
    }

    // ----------------- ENTRADAS -----------------
    private JPanel entradasPanel() {
        JPanel p = new JPanel(new BorderLayout());
        modelEntradas = new DefaultTableModel(new String[]{"ID","Asiento","Funcion","Cliente","Valor","Tipo Pago","FechaCompra"},0){
            public boolean isCellEditable(int r, int c){ return false; }
        };
        tableEntradas = new JTable(modelEntradas);
        p.add(new JScrollPane(tableEntradas), BorderLayout.CENTER);

        JPanel botones = new JPanel();
        JButton add = new JButton("Agregar");
        JButton edit = new JButton("Editar");
        JButton del = new JButton("Eliminar");
        JButton ref = new JButton("Refrescar");
        botones.add(add); botones.add(edit); botones.add(del); botones.add(ref);
        p.add(botones, BorderLayout.SOUTH);

        add.addActionListener(_ -> dialogEntrada(null));
        edit.addActionListener(_ -> {
            int sel = tableEntradas.getSelectedRow(); if(sel==-1){ JOptionPane.showMessageDialog(this,"Selecciona una entrada"); return; }
            int id = (Integer) tableEntradas.getValueAt(sel,0);
            dialogEntrada(ModelService.getEntradaById(id));
        });
        del.addActionListener(_ -> {
            int sel = tableEntradas.getSelectedRow(); if(sel==-1){ JOptionPane.showMessageDialog(this,"Selecciona una entrada"); return; }
            int id = (Integer) tableEntradas.getValueAt(sel,0);
            if(JOptionPane.showConfirmDialog(this,"Eliminar entrada ID "+id+"?","Confirmar",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
                if(ModelService.deleteEntrada(id)) refreshEntradasTable(); else JOptionPane.showMessageDialog(this,"Error al eliminar");
            }
        });
        ref.addActionListener(_ -> refreshEntradasTable());
        return p;
    }

    private void dialogEntrada(Entrada e) {
        // comboboxes: asientos, funciones, clientes
        Map<Integer,String> mapAs = ModelService.mapAsientos();
        Map<Integer,String> mapFu = ModelService.mapFunciones();
        Map<Integer,String> mapCl = ModelService.mapClientes();

        JComboBox<String> cbAsiento = new JComboBox<>(mapAs.values().toArray(new String[0]));
        JComboBox<String> cbFuncion = new JComboBox<>(mapFu.values().toArray(new String[0]));
        JComboBox<String> cbCliente = new JComboBox<>(mapCl.values().toArray(new String[0]));
        JTextField txtValor = new JTextField();
        JTextField txtTipoPago = new JTextField();
        JTextField txtFecha = new JTextField(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        java.util.List<Integer> asIds = new ArrayList<>(mapAs.keySet());
        java.util.List<Integer> fuIds = new ArrayList<>(mapFu.keySet());
        java.util.List<Integer> clIds = new ArrayList<>(mapCl.keySet());

        if(e!=null) {
            // seleccionar índice correspondente
            int idxAs = asIds.indexOf(e.asiento_id); if(idxAs>=0) cbAsiento.setSelectedIndex(idxAs);
            int idxFu = fuIds.indexOf(e.funcion_id); if(idxFu>=0) cbFuncion.setSelectedIndex(idxFu);
            int idxCl = clIds.indexOf(e.cliente_id); if(idxCl>=0) cbCliente.setSelectedIndex(idxCl);
            txtValor.setText(String.valueOf(e.valor)); txtTipoPago.setText(e.tipo_pago); txtFecha.setText(e.fecha_compra);
        } else {
            if(!clIds.isEmpty()) cbCliente.setSelectedIndex(0);
            if(!asIds.isEmpty()) cbAsiento.setSelectedIndex(0);
            if(!fuIds.isEmpty()) cbFuncion.setSelectedIndex(0);
        }

        Object[] fields = {
                "Asiento:", cbAsiento,
                "Función:", cbFuncion,
                "Cliente:", cbCliente,
                "Valor:", txtValor,
                "Tipo de pago:", txtTipoPago,
                "Fecha compra (yyyy-MM-dd):", txtFecha
        };
        int r = JOptionPane.showConfirmDialog(this, fields, e==null?"Agregar Entrada":"Editar Entrada", JOptionPane.OK_CANCEL_OPTION);
        if(r==JOptionPane.OK_OPTION) {
            try {
                int selAs = cbAsiento.getSelectedIndex();
                int selFu = cbFuncion.getSelectedIndex();
                int selCl = cbCliente.getSelectedIndex();
                int asientoId = asIds.isEmpty() ? 0 : asIds.get(selAs);
                int funcionId = fuIds.isEmpty() ? 0 : fuIds.get(selFu);
                int clienteId = clIds.isEmpty() ? 0 : clIds.get(selCl);
                double valor = Double.parseDouble(txtValor.getText().trim().isEmpty() ? "0" : txtValor.getText().trim());
                String tipoPago = txtTipoPago.getText().trim();
                String fecha = txtFecha.getText().trim();

                if(e==null) {
                    Entrada ne = new Entrada();
                    ne.asiento_id = asientoId; ne.funcion_id = funcionId; ne.cliente_id = clienteId; ne.valor = valor; ne.tipo_pago = tipoPago; ne.fecha_compra = fecha;
                    if(ModelService.insertEntrada(ne)) refreshEntradasTable(); else JOptionPane.showMessageDialog(this,"Error al insertar entrada");
                } else {
                    e.asiento_id = asientoId; e.funcion_id = funcionId; e.cliente_id = clienteId; e.valor = valor; e.tipo_pago = tipoPago; e.fecha_compra = fecha;
                    if(ModelService.updateEntrada(e)) refreshEntradasTable(); else JOptionPane.showMessageDialog(this,"Error al actualizar entrada");
                }
            } catch (Exception ex) { JOptionPane.showMessageDialog(this,"Error: " + ex.getMessage()); }
        }
    }

    private void refreshEntradasTable() {
        modelEntradas.setRowCount(0);
        // mostramos descripcion de asiento/funcion/cliente usando mapas
        Map<Integer,String> asMap = ModelService.mapAsientos();
        Map<Integer,String> fuMap = ModelService.mapFunciones();
        Map<Integer,String> clMap = ModelService.mapClientes();
        for (Entrada en : ModelService.getAllEntradas()) {
            String as = asMap.getOrDefault(en.asiento_id, ""+en.asiento_id);
            String fu = fuMap.getOrDefault(en.funcion_id, ""+en.funcion_id);
            String cl = clMap.getOrDefault(en.cliente_id, ""+en.cliente_id);
            modelEntradas.addRow(new Object[]{en.id, as, fu, cl, en.valor, en.tipo_pago, en.fecha_compra});
        }
    }

    // ----------------- FACTURAS -----------------
    private JPanel facturasPanel() {
        JPanel p = new JPanel(new BorderLayout());
        modelFacturas = new DefaultTableModel(new String[]{"ID","Cliente","Empresa","Documento","Contacto","Fecha","Tipo Pago","Valor Total"},0) {
            public boolean isCellEditable(int r,int c){ return false; }
        };
        tableFacturas = new JTable(modelFacturas);
        p.add(new JScrollPane(tableFacturas), BorderLayout.CENTER);

        JPanel botones = new JPanel();
        JButton add = new JButton("Agregar");
        JButton verDetalles = new JButton("Ver Detalle");
        JButton edit = new JButton("Editar");
        JButton del = new JButton("Eliminar");
        JButton ref = new JButton("Refrescar");
        botones.add(add); botones.add(verDetalles); botones.add(edit); botones.add(del); botones.add(ref);
        p.add(botones, BorderLayout.SOUTH);

        add.addActionListener(_ -> dialogFactura(null));
        verDetalles.addActionListener(_ -> {
            int sel = tableFacturas.getSelectedRow(); if(sel==-1){ JOptionPane.showMessageDialog(this,"Selecciona una factura"); return; }
            int id = (Integer) tableFacturas.getValueAt(sel,0);
            java.util.List<DetalleFactura> detalles = ModelService.getDetalleByFacturaId(id);
            if(detalles.isEmpty()) JOptionPane.showMessageDialog(this,"Factura sin detalle");
            else {
                StringBuilder sb = new StringBuilder();
                for(DetalleFactura d : detalles) sb.append("ID Detalle:").append(d.id).append(" EntradaID:").append(d.entrada_id)
                        .append(" Cant:").append(d.cantidad).append(" V.unit:").append(d.valor_unitario).append(" Total:").append(d.valor_total).append("\n");
                JOptionPane.showMessageDialog(this, sb.toString(), "Detalle factura " + id, JOptionPane.INFORMATION_MESSAGE);
            }

            
        });
        edit.addActionListener(_ -> {
            int sel = tableFacturas.getSelectedRow(); if(sel==-1){ JOptionPane.showMessageDialog(this,"Selecciona una factura"); return; }
            int id = (Integer) tableFacturas.getValueAt(sel,0);
            dialogFactura(ModelService.getFacturaById(id));
        });
        del.addActionListener(_ -> {
            int sel = tableFacturas.getSelectedRow(); if(sel==-1){ JOptionPane.showMessageDialog(this,"Selecciona una factura"); return; }
            int id = (Integer) tableFacturas.getValueAt(sel,0);
            if(JOptionPane.showConfirmDialog(this,"Eliminar factura ID "+id+"?","Confirmar",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
                if(ModelService.deleteFactura(id)) refreshFacturasTable(); else JOptionPane.showMessageDialog(this,"Error al eliminar");
            }
        });
        ref.addActionListener(_ -> refreshFacturasTable());

        return p;
    }

    private void dialogFactura(Factura f) {
        Map<Integer, String> clMap = ModelService.mapClientes();
        java.util.List<Integer> clIds = new ArrayList<>(clMap.keySet());
        JComboBox<String> cbCliente = new JComboBox<>(clMap.values().toArray(new String[0]));
        JTextField txtEmpresa = new JTextField();
        JTextField txtDoc = new JTextField();
        JTextField txtContacto = new JTextField();
        JTextField txtFecha = new JTextField(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        JTextField txtTipoPago = new JTextField();
        JTextField txtValorTotal = new JTextField("0");

        if(f!=null) {
            int idx = clIds.indexOf(f.cliente_id); if(idx>=0) cbCliente.setSelectedIndex(idx);
            txtEmpresa.setText(f.empresa_nombre); txtDoc.setText(f.empresa_documento); txtContacto.setText(f.contacto);
            txtFecha.setText(f.fecha); txtTipoPago.setText(f.tipo_pago); txtValorTotal.setText(String.valueOf(f.valor_total));
        }

        Object[] fields = {
                "Cliente:", cbCliente,
                "Empresa (opcional):", txtEmpresa,
                "Documento empresa:", txtDoc,
                "Contacto:", txtContacto,
                "Fecha (yyyy-MM-dd):", txtFecha,
                "Tipo pago:", txtTipoPago,
                "Valor total:", txtValorTotal,
                "Nota: Puedes luego 'Agregar detalle' seleccionando la factura y usar IDs de entradas para vincularlas."
        };

        int r = JOptionPane.showConfirmDialog(this, fields, f==null?"Agregar Factura":"Editar Factura", JOptionPane.OK_CANCEL_OPTION);
        if(r==JOptionPane.OK_OPTION) {
            int selCl = cbCliente.getSelectedIndex();
            int clienteId = clIds.isEmpty() ? 0 : clIds.get(selCl);
            String empresa = txtEmpresa.getText().trim();
            String doc = txtDoc.getText().trim();
            String contacto = txtContacto.getText().trim();
            String fecha = txtFecha.getText().trim();
            String tipoPago = txtTipoPago.getText().trim();
            double valorTotal = 0; try{ valorTotal = Double.parseDouble(txtValorTotal.getText().trim()); } catch(Exception ex) { valorTotal = 0; }

            if(f==null) {
                Factura nf = new Factura(); nf.cliente_id = clienteId; nf.empresa_nombre = empresa; nf.empresa_documento = doc; nf.contacto = contacto; nf.fecha = fecha; nf.tipo_pago = tipoPago; nf.valor_total = valorTotal;
                if(ModelService.insertFactura(nf)) {
                    JOptionPane.showMessageDialog(this,"Factura creada. Para registrar detalle: selecciona factura y usa la opción 'Ver Detalle' / administrar detalle desde DB.");
                    refreshFacturasTable();
                } else JOptionPane.showMessageDialog(this,"Error al crear factura");
            } else {
                f.cliente_id = clienteId; f.empresa_nombre = empresa; f.empresa_documento = doc; f.contacto = contacto; f.fecha = fecha; f.tipo_pago = tipoPago; f.valor_total = valorTotal;
                if(ModelService.updateFactura(f)) refreshFacturasTable(); else JOptionPane.showMessageDialog(this,"Error al actualizar factura");
            }
        }
    }

    private void refreshFacturasTable() {
        modelFacturas.setRowCount(0);
        Map<Integer,String> clMap = ModelService.mapClientes();
        for(Factura f : ModelService.getAllFacturas()) {
            String cliente = clMap.getOrDefault(f.cliente_id, ""+f.cliente_id);
            modelFacturas.addRow(new Object[]{f.id, cliente, f.empresa_nombre, f.empresa_documento, f.contacto, f.fecha, f.tipo_pago, f.valor_total});
        }
    }
}
