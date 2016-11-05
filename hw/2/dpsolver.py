import knapsacksolver
import numpy as np


class DPSolver(knapsacksolver.KnapsackSolver):
    def __init__(self, data, capacity):
        super().__init__(data, capacity)

        self.matrix = np.zeros(shape=(capacity + 1, len(self.data_raw) + 1))
        self.init_matrix()

    def init_matrix(self):
        for idx, item in enumerate(self.data_raw):
            self.matrix[0, idx + 1] = item[1]
            self.matrix[1, idx + 1] = item[0]

    def solve_bag(self):
        print("dp items:    {}".format((len(self.data_raw) + 1) * (self.capacity + 1)))
        for i in range(1, len(self.data_raw) + 1):
            for j in range(0, self.capacity + 1):
                cur_weight = self.data_raw[i - 1][1]
                cur_val = self.data_raw[i - 1][0]

                # Value if this item is NOT added
                prev_value = self.matrix[j, i - 1]

                # Value if this item is added alongside the optimal solution that fits
                # (add this item and look at optimal solution for weight "j-current"
                allowed_cur_val = 0
                if cur_weight <= j:
                    allowed_cur_val = cur_val

                prev_optimal_val = 0
                if j - cur_weight >= 0:
                    prev_optimal_val = self.matrix[j - cur_weight, i - 1]

                new_value = max(prev_value, allowed_cur_val + prev_optimal_val)
                self.matrix[j, i] = new_value

        return self.matrix[self.capacity, len(self.data_raw)]
