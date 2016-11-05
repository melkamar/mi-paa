import time
import numpy
import os
import bbsolver
import dpsolver
import fptassolver


def solve_knapsack_by_weight(data):
    """
    :param data: List of tuples: (value, weight)
    :return:
    """


def parse_data_line(text):
    """
    Parses an instance from a source file.
    :param text: The line to parse.
    :return: Tuple of (ID, size, bag_capacity, [(val1, weight1), (val2, weight2), ...])
    """
    data = text.split(" ")
    id = int(data[0])
    count = int(data[1])
    capacity = int(data[2])

    values = []
    for i in range(3, len(data), 2):
        values.append((int(data[i + 1]), int(data[i])))

    return id, count, capacity, values


# files = ['knap_40.inst.dat']
files = ['knap_4.inst.dat', 'knap_10.inst.dat', 'knap_15.inst.dat', 'knap_20.inst.dat', 'knap_22.inst.dat',
         'knap_25.inst.dat', 'knap_27.inst.dat', 'knap_30.inst.dat', 'knap_32.inst.dat', 'knap_35.inst.dat',
         'knap_37.inst.dat', 'knap_40.inst.dat']

# (27 38) (2 86) (41 112) (1 0) (25 66) (1 97) (34 195) (3 85) (50 42) (12 223)

fptas_max_rel_errors = numpy.arange(0.1, 0.8, 0.1)
# fptas_max_rel_errors = [0]

for fn in [os.path.join('data', fn) for fn in files]:
    with open(fn) as file:
        bb_time_per_file = 0
        dp_time_per_file = 0

        fptas_err_times_file = {}
        for item in fptas_max_rel_errors:
            fptas_err_times_file[item] = 0

        for line_idx, line in enumerate(file):
            data = parse_data_line(line)
            # print("=== new line: {}===".format(line_idx))
            # bbsolve
            # solver = bbsolver.BBSolver(data[3], data[2])
            # bb_time_start = time.process_time()
            # bb_res = solver.solve_bag()
            # bb_time_end = time.process_time()
            # bb_time_per_file += bb_time_end - bb_time_start
            # print(" bbsolve: {}".format(bb_res))

            # dpsolve
            # solver = dpsolver.DPSolver(data[3], data[2])
            solver = fptassolver.FPTASSolver(data[3], data[2])
            dp_time_start = time.process_time()
            dp_res = solver.solve_bag()
            dp_time_end = time.process_time()
            dp_time_per_file += dp_time_end - dp_time_start
            # print(" dpsolve: {}".format(dp_res))

            # fptas solver
            for e in fptas_max_rel_errors:
                solver = fptassolver.FPTASSolver(data[3], data[2], max_rel_err=e, name="{} #{} e={}".format(fn, line_idx, e))
                fptas_time_start = time.process_time()
                fptas_res = solver.solve_bag()
                fptas_time_end = time.process_time()

                prev_time = fptas_err_times_file[e]
                fptas_err_times_file[e] = prev_time + fptas_time_end - fptas_time_start

            # print(" fpsolve: {}".format(fptas_res))

            # break

            # if bb_res != dp_res or dp_res != fptas_res or bb_res != fptas_res:
            #     print("RESULTS DONT MATCH!")
            #     break

            # print("Time: {}".format(time_file / 50.0))

        lines_count = line_idx + 1
        bb_avg_time_instance = bb_time_per_file / lines_count
        # print("{} - avg time BB: {}".format(fn, bb_avg_time_instance))
        dp_avg_time_instance = dp_time_per_file / lines_count
        # print("{} - avg time DP: {}".format(fn, dp_avg_time_instance))
        for key in fptas_err_times_file:
            avg_time = fptas_err_times_file.get(key) / lines_count
            # print("{} - avg time FPTAS: {}  e={}".format(fn, avg_time, key))

        print("""REPORT for file: {}
        """.format(fn))
        print("""
        Total times:
          BB: {}
          DP: {}
          FPTAS (err:time): {}
        """.format(bb_time_per_file, dp_time_per_file, fptas_err_times_file))

        print("""
        Average times:
          BB: {}
          DP: {}
          FPTAS (err:time): {}
        """.format(bb_avg_time_instance,
                   dp_avg_time_instance,
                   ["e={} t={}".format(key, fptas_err_times_file.get(key) / lines_count) for key in
                    sorted(fptas_err_times_file.keys())]))

        print("\n\n")
