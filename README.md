# OS Simulator (Java)

**Engineered by Yug Arora**

A modular Operating Systems (OS) simulator written in Java.

It contains small, focused implementations of classic OS topics (CPU scheduling, paging, memory allocation, disk scheduling, synchronization, deadlock detection) with a CLI to run demos and an analysis layer to compare results.

![OS Simulator Architecture](architecture.png)

## What’s inside

**Algorithms & demos**
- CPU Scheduling: FCFS, Round Robin (+ Gantt-style output)
- Paging: FIFO, LRU (+ step-by-step logs)
- Memory Allocation: First Fit, Best Fit, Worst Fit (+ compaction + fragmentation metrics)
- Disk Scheduling: FCFS, SSTF, SCAN, C-SCAN (+ head-movement graph)
- Synchronization: Producer–Consumer, Readers–Writers, Dining Philosophers (thread-based)
- Deadlock: Resource Allocation Graph → Wait-For Graph, cycle detection

**Extras**
- `cli/` menu-driven scenarios
- `analysis/` metrics + comparison reports + CSV exporter

## Project structure

- `cli/` — interactive menu + scenario runner
- `scheduling/` — FCFS, Round Robin
- `paging/` — FIFO, LRU + comparators/formatters
- `memory/` — allocators + reports/validation
- `disk/` — disk schedulers + comparison + graphs
- `synchronization/` — semaphore + classic synchronization problems
- `deadlock/` — RAG/WFG + deadlock detector
- `analysis/` — metrics + report/CSV export
- `models/`, `utils/` — shared data models and helpers

## Quickstart (Windows PowerShell)

**Prerequisites**
- JDK (Java) installed (`javac` and `java` available in PATH)

**Build** (run from the project root)
```powershell
javac -d bin .\Main.java `
  .\models\*.java `
  .\utils\*.java `
  .\scheduling\*.java `
  .\paging\*.java `
  .\memory\*.java `
  .\disk\*.java `
  .\synchronization\*.java .\synchronization\producerconsumer\*.java `
  .\synchronization\readerswriters\*.java .\synchronization\philosophers\*.java `
  .\deadlock\*.java `
  .\analysis\*.java `
  .\cli\*.java
```

**Run the CLI**
```powershell
java -cp bin cli.Application
```

## Example runs (direct entry points)

```powershell
java -cp bin Main
java -cp bin paging.PagingComparison
java -cp bin memory.MemoryValidation
java -cp bin disk.DiskComparison
java -cp bin synchronization.producerconsumer.ProducerConsumerSimulator
java -cp bin synchronization.readerswriters.ReadersWritersSimulator
java -cp bin synchronization.philosophers.DiningPhilosophersSimulator
```

## Documentation

- Architecture overview: see `ARCHITECTURE.md`

## Notes / troubleshooting

- Run commands from the project root so `-cp bin` resolves correctly.
- Disk scheduling assumes track indices are in the range `0` to `diskSize - 1`.
- Synchronization demos are thread-based, so output ordering may interleave.