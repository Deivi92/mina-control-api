
import { Box, IconButton, Tooltip, Chip } from '@mui/material';
import { DataGrid, GridColDef, GridActionsCellItem } from '@mui/x-data-grid';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import { TipoTurno } from '../types';

interface Props {
  turnos: TipoTurno[];
  onEdit: (turno: TipoTurno) => void;
  onDelete: (id: number) => void;
  isLoading: boolean;
}

export const TiposTurnoTable = ({ turnos, onEdit, onDelete, isLoading }: Props) => {
  const columns: GridColDef[] = [
    {
      field: 'nombre',
      headerName: 'Nombre',
      flex: 1,
      minWidth: 150,
    },
    {
      field: 'horaInicio',
      headerName: 'Hora de Inicio',
      flex: 1,
    },
    {
      field: 'horaFin',
      headerName: 'Hora de Fin',
      flex: 1,
    },
    {
        field: 'color',
        headerName: 'Color',
        flex: 0.5,
        renderCell: (params) => (
            <Chip sx={{ backgroundColor: params.value, width: '24px', height: '24px' }} />
        ),
    },
    {
      field: 'actions',
      type: 'actions',
      headerName: 'Acciones',
      width: 100,
      cellClassName: 'actions',
      getActions: ({ row }) => {
        return [
          <GridActionsCellItem
            icon={<Tooltip title="Editar"><EditIcon /></Tooltip>}
            label="Editar"
            onClick={() => onEdit(row)}
            color="inherit"
          />,
          <GridActionsCellItem
            icon={<Tooltip title="Eliminar"><DeleteIcon /></Tooltip>}
            label="Eliminar"
            onClick={() => onDelete(row.id)}
            color="inherit"
          />,
        ];
      },
    },
  ];

  return (
    <Box sx={{ height: 400, width: '100%' }}>
      <DataGrid
        rows={turnos}
        columns={columns}
        loading={isLoading}
        initialState={{
          pagination: {
            paginationModel: { page: 0, pageSize: 5 },
          },
        }}
        pageSizeOptions={[5, 10]}
        disableRowSelectionOnClick
        localeText={{
            noRowsLabel: 'No se encontraron tipos de turno',
        }}
      />
    </Box>
  );
};
