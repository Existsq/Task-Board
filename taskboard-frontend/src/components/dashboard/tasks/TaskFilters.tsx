import { Box, FormControl, FormLabel, Select, Option } from "@mui/joy";

export interface Filters {
  status?: "NEW" | "IN_PROGRESS" | "COMPLETED";
  sort?: "createdAt" | "title";
  direction?: "asc" | "desc";
}

interface TaskFiltersProps {
  filters: Filters;
  onFilterChange: (filters: Filters) => void;
}

export default function TaskFilters({ filters, onFilterChange }: TaskFiltersProps) {
  return (
    <Box
      sx={{
        borderRadius: "sm",
        py: 2,
        display: { xs: "none", sm: "flex" },
        flexWrap: "wrap",
        gap: 1.5,
        "& > *": { minWidth: { xs: "120px", md: "160px" } },
      }}
    >

      <FormControl size="sm">
        <FormLabel>Status</FormLabel>
        <Select
          value={filters.status || ""}
          onChange={(_, newValue) => {
            const validStatuses = ["NEW", "IN_PROGRESS", "COMPLETED"] as const;
            const status = newValue && validStatuses.includes(newValue as any)
              ? newValue as Filters["status"]
              : undefined;
            onFilterChange({ ...filters, status });
          }}
          size="sm"
          placeholder="Filter by status"
        >
          <Option value="">All</Option>
          <Option value="NEW">New</Option>
          <Option value="IN_PROGRESS">In progress</Option>
          <Option value="COMPLETED">Completed</Option>
        </Select>
      </FormControl>

      {/* Сортировка по полю */}
      <FormControl size="sm">
        <FormLabel>Sort By</FormLabel>
        <Select
          value={filters.sort || ""}
          onChange={(_, newValue) => {
            const validSorts = ["createdAt", "title"] as const;
            const sort = newValue && validSorts.includes(newValue as any)
              ? newValue as Filters["sort"]
              : undefined;
            onFilterChange({ ...filters, sort });
          }}
          size="sm"
          placeholder="Sort by"
        >
          <Option value="createdAt">Date</Option>
          <Option value="title">Title</Option>
        </Select>
      </FormControl>

      {/* Направление сортировки */}
      <FormControl size="sm">
        <FormLabel>Direction</FormLabel>
        <Select
          value={filters.direction || "asc"}
          onChange={(_, newValue) => {
            const validDirections = ["asc", "desc"] as const;
            const direction = newValue && validDirections.includes(newValue as any)
              ? newValue as Filters["direction"]
              : "asc";
            onFilterChange({ ...filters, direction });
          }}
          size="sm"
          placeholder="Sort direction"
        >
          <Option value="asc">Ascending</Option>
          <Option value="desc">Descending</Option>
        </Select>
      </FormControl>
    </Box>
  );
}