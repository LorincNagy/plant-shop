import React from 'react';
import Typography from '@mui/material/Typography';
import Pagination from '@mui/material/Pagination';
import Stack from '@mui/material/Stack';

export default function PaginationControlled({ totalProducts, pageSize, page, onChange }) {
    return (
        <Stack spacing={2} sx={{ marginTop: '2em' }}>
            <Typography fontSize={18}>Page: {page}</Typography>
            <Pagination
                count={Math.ceil(totalProducts / pageSize)}
                page={page}
                onChange={onChange}
                size="large"
            />
        </Stack>
    );
}
